
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
    },
    {
        firstName: "Nick",
        lastName: "Drake",
        birthDate: "1948-06-19"
    }
] );

printjson( db.singers.find( {} ) );
