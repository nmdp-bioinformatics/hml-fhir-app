(function () {
    var dbName = 'nmdp_interop',
        client = new Mongo(),
        db = client.getDB(dbName);

    var collections = [
        {
            name: 'Hml.TypingTestNames',
            defaultData: [
                { name: 'Illumina 454', description: 'Illumina 454 sequencing', active: true, dateCreated: new Date() },
                { name: 'RNA Seq', description: 'RNA Seq sequencing', active: true, dateCreated: new Date()},
                { name: 'Ion Torrent', description: 'Ion Torrent sequencing', active: true, dateCreated: new Date() }
            ]
        },
        {
            name: 'Hml.ReportingCenters',
            defaultData: [
                { context: 'NIH Designated Cancer Center', active: true, dateCreated: new Date() },
                { context: 'Mayo Clinic Foundation', active: true, dateCreated: new Date() },
                { context: 'Cleveland Clinic Foundation', active: true, dateCreated: new Date() }
            ]
        },
        {
            name: 'Hml.Samples.CollectionMethods',
            defaultData: [
                { name: 'Cheek swab', description: 'DNA obtained through inner cheek swab', active: true, dateCreated: new Date() }
            ]
        },
        {
            name: 'Hml.Versions',
            defaultData: [
                { name: '1.0.1', description: 'Default HML Version', active: true, dateCreated: new Date() }
            ]
        }
    ];

    for (var i = 0; i < collections.length; i++) {
        var collection = db.getCollection(collections[i].name);

        db.createCollection(collections[i].name);
        collection.insert(collections[i].defaultData);
    }
}());