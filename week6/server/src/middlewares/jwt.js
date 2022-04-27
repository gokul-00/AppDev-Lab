const jwt = require('jsonwebtoken')

require('dotenv').config({ path: '../env/.env' })

const createJWTtoken = async (username) => {
    if (username)
        return jwt.sign(
            {
                username: username
            },
            process.env.TOKEN_SECRET,
            { expiresIn: '720h' }
        )
}

const verifyUserJWT = (req, res, next) => {
    try {
        const { token } = req.headers
        // if token is undefined
        if (!token) {
            return res.status(401).json({ message: 'No token' })
        }

        jwt.verify(token, process.env.TOKEN_SECRET, (err, decoded) => {
            req.jwt_payload = decoded

            if (err) {
                return res.status(401).json({ message: 'Invalid token or Token expired' })
            }

            if (!decoded.username) {
                return res.status(400).json({ message: 'Invalid token' })
            }

            return next()
        })
    } catch (err) {
        console.log(err.message)

        return res.status(500).json({ message: 'Server Error. Try agin later!' })
    }
}

const createOTPtoken = async (username) => {
    if (username)
        return jwt.sign(
            {
                username: username,
                otpVerified: true
            },
            process.env.TOKEN_SECRET,
            { expiresIn: '600s' }
        )
}

const verifyUserOTP = (req, res, next) => {
    try {
        const { otptoken } = req.headers
        // if token is undefined
        if (!otptoken) {
            return res.status(401).json({ message: 'No token' })
        }
        jwt.verify(otptoken, process.env.TOKEN_SECRET, (err, decoded) => {
            req.jwt_payload = decoded
            if (err) {
                return res.status(403).json({ message: 'Invalid token or Token expired' })
            }
            if (!decoded.otpVerified) return res.status(400).json({ message: 'Invalid token' })
            return next()
        })
    } catch (err) {
        console.log(err.message)

        return res.status(500).json({ message: 'Server Error. Try agin later!' })
    }
}

module.exports = {
    createJWTtoken,
    verifyUserJWT,
    createOTPtoken,
    verifyUserOTP
}
