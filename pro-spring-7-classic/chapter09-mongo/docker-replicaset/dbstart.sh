#!/bin/bash

docker-compose up -d

sleep 10

docker exec mongo1 mongosh <<EOF
use admin
db.createUser(
   {
       user: 'prospring7',
       pwd: 'prospring7',
       roles: [
           {
               role: 'dbOwner',
               db: 'musicdb'
           } ]
} );
printjson( db.getUsers() );
db = connect( 'mongodb://prospring7:prospring7@localhost:27017/musicdb?authSource=admin' );
db.createCollection('singers', { capped: false });

db.singers.insertMany( [
   {
       firstName: "John",
       lastName: "Mayer",
       birthDate: "1977-10-16"
   } ,
   {
       firstName: "Ben",
       lastName: "Barnes",
       birthDate: "1981-08-20"
   },
   {
       firstName: "John",
       lastName: "Butler",
       birthDate: "1975-04-01"
   }
] );

printjson( db.singers.find( {} ) );
EOF
