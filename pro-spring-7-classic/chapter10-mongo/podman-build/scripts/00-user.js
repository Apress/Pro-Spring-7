db = connect( 'mongodb://root:mainpass@localhost:27017/admin' );

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
