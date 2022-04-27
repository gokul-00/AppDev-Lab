const usernameElement = document.getElementById('username')
const passwordElement = document.getElementById('password')
const submitButton = document.getElementById('submit')

submitButton.addEventListener('click', async (e) => {
    e.preventDefault()
    const username = usernameElement.value
    const password = passwordElement.value
    const creds = {
        username,
        password
    }
    try {
        const response = await fetch('http://localhost:3000/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(creds)
        })
        const data = await response.json()
        console.log(data)
        if (response.ok) {
            localStorage.setItem('token', data.token)
            window.location.href = 'user.html'
        } else {
            throw new Error(data.message)
        }
    } catch (e) {
        console.log(e.message)
        alert(e.message)
    }
})
