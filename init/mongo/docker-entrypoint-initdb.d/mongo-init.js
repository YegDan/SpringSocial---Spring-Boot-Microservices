
db = db.getSiblingDB('post-service')

db.createUser({
    user: 'rootadmin',
    pwd: 'password',
    roles: [{role: 'readWrite', db: 'post-service'}]
})

print('END')






// print('START')
//
// db = db.getSiblingDB('user-service')
//
// db.createUser({
//     user: 'admin',
//     pwd: 'password',
//     roles: [{role: 'readWrite', db: 'user-service'}]
// })
//
// db.createCollection('user')
//
// print('END')
//
// db = db.getSiblingDB('comment-service')
//
// db.createUser({
//     user: 'admin',
//     pwd: 'password',
//     roles: [{role: 'readWrite', db: 'comment-service'}]
// })
//
// db.createCollection('comment')
//
// print('END')