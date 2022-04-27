const nodemailer = require("nodemailer");
const sendMail = (otp, email) => {
  let mailTransporter = nodemailer.createTransport({
    service: "gmail",
    auth: {
      user: "ygokul1702@gmail.com",
      pass: "Gokul@Gmail2050",
    },
  });
  console.log(otp);
  let mailDetails = {
    from: "ygokul1702@gmail.com",
    to: email,
    subject: "OTP",
    text: `${otp}`,
  };

  mailTransporter.sendMail(mailDetails, function (err, data) {
    if (err) {
      console.log("Error Occurs");
    } else {
      console.log("Email sent successfully");
    }
  });
};

module.exports = sendMail;
