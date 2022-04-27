const api = require('express').Router()

const dashboardRouter = require('./routers/dashboard.js')
const userAuthRouter = require('./routers/userAuth.js')
const editRouter = require('./routers/editDetails.js')

const { verifyUserJWT } = require('../middlewares/jwt')

api.use('/user', verifyUserJWT, dashboardRouter)
api.use('/edit', verifyUserJWT, editRouter)
api.use('/auth', userAuthRouter)

module.exports = api

// /auth/login
// /auth/register

// /user

// /edit/sendOtp
// /edit/verifyOtp
// /edit/editDetails
