db = db.getSiblingDB("expensio-db");

db.createUser({
    user: "expensio_user",
    pwd: "expensio_pass",
    roles: [
        {
            role: "readWrite",
            db: "expensio-db"
        }
    ]
});
