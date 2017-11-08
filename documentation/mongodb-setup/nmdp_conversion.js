(function () {
    var dbName = 'nmdp_conversion',
        client = new Mongo(),
        db = client.getDB(dbName);

    var collections = [
        { name: 'conversionStatus' },
        { name: 'hml' },
        { name: 'fhir' }
    ];

    for (var i = 0; i < collections.length; i++) {
        var collection = db.getCollection(collections[i].name);

        db.createCollection(collections[i].name);
        collection.insert(collections[i].defaultData);
    }
}());