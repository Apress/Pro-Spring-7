/*
Freeware License, some rights reserved

Copyright (c) 2025 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy
of this software and associated documentation files (the "Software"),
to work with the Software within the limits of freeware distribution and fair use.
This includes the rights to use, copy, and modify the Software for personal use.
Users are also allowed and encouraged to submit corrections and modifications
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for
commercial use in any way, or for a user's educational materials such as books
or blog articles without prior permission from the copyright holder.

The above copyright notice and this permission notice need to be included
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.apress.prospring7.classic.nine;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

///
/// @author iulianacosmina on 27/12/2025
///
public class MongoReplicaSet {
    private static final String MONGO_IMAGE = "mongo:latest";
    private static final String RS_NAME = "rs0";

    private List<PortAndContainer> nodes = new ArrayList<>();

    record PortAndContainer(int port, GenericContainer container) {}

    public MongoReplicaSet() { this(3);}

    public MongoReplicaSet(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("At least one node is required");
        }

        for (int i = 0; i < n; i++) {
            var port = findAvailableTcpPort();
            var node = new GenericContainer(MONGO_IMAGE)//
                    //.withNetworkMode("host")//
                    .withExtraHost("host.podman.internal", "host-gateway")
                    .waitingFor(Wait.forLogMessage(".*Waiting for connections.*\\n", 1))//
                    .withCommand("mongod", "--replSet", RS_NAME, "--bind_ip_all", "--port", String.valueOf(port));
            nodes.add(new PortAndContainer(port, node));
        }
    }

    public String getConnectionString() {
        return nodes.stream()//
                .map(node -> format("%s:%d", node.container().getHost(), node.port()))
                .collect(Collectors.joining(",", "mongodb://", "/?replicaSet=" + RS_NAME));
    }

    public void start() {
        nodes.forEach(node -> node.container().start());

        var replicaMembersCfg = nodes.stream()//
                .map(node -> format("{\"_id\": %d, \"host\": \"%s:%d\"}", nodes.indexOf(node),
                        // we use host.podman.internal, because this one is in /etc/hosts
                        node.container().getExtraHosts().getFirst().toString().split(":")[0], node.port()))
                .collect(Collectors.joining(","));
        var rsInitiate = format("rs.initiate({\"_id\": \"%s\", \"members\": [%s]})", RS_NAME, replicaMembersCfg);

        var node = nodes.get(0);
        // we use host.podman.internal, because this one is in /etc/hosts
        var nodeUrl = node.container().getExtraHosts().getFirst().toString().split(":")[0] + ":" + node.port();

        execInContainer(node.container(), "mongosh", nodeUrl + "/admin", "--quiet", "--eval", rsInitiate);
        execInContainer(node.container(), "/bin/bash", "-c",
                "until mongosh " + nodeUrl + "/admin"
                        + " --eval \"printjson(rs.isMaster())\" | grep ismaster | grep true > /dev/null 2>&1;"
                        + "do sleep 1;done");
    }

    private void execInContainer(GenericContainer container, String... command) {
        try {
            var result = container.execInContainer(command);
            if (result.getExitCode() != 0) {
                throw new RuntimeException(format("Failed execution of command. Code: %s, output: %s ",
                        result.getExitCode(), result.getStdout() + "\n" + result.getStderr()));
            }
        } catch (UnsupportedOperationException | IOException | InterruptedException e) {
            throw new RuntimeException("Failed to execute command", e);
        }
    }

    public void stop() {
        nodes.forEach(node -> node.container().stop());
    }

    private int findAvailableTcpPort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to find available port", e);
        }
    }
}
