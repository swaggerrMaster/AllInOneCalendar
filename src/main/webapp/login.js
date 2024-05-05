
// function to register to user
function registerUser() {
    event.preventDefault(); // NOT defusalt
    
    console.log("RegisterFUNCTION");
    
    var formData = {
        email: document.getElementById('emailR').value,
        username: document.getElementById('usernameR').value,
        name: document.getElementById('nameR').value,
        password: document.getElementById('passwordR').value,
        confirm_password: document.getElementById('confirmPasswordR').value
    };
    
    console.log(formData.email + ":" + formData.name);

    if (formData.password !== formData.confirm_password) {
        document.getElementById("messageE").innerText = "Passwords do not match.";
        return;
    }

    fetch('RegisterServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: formData.email,
            name: formData.name,
            username: formData.username,
            password: formData.password
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data == "Successful") {
			// sets storgae stuff to use later
            console.log("Registration successful");
            window.location.href = 'index.html';
            sessionStorage.setItem("loggedIn", "true");
            sessionStorage.setItem("name", formData.name);
            sessionStorage.setItem("username", formData.username);
        } else {
            document.getElementById("messageE").innerText = "Error- something is taken";
        }
    })
    .catch(error => {
        console.error('Error during registration:', error);
        document.getElementById("messageE").innerText = "Registration failed. Please try again.";
    });
}


// function to login user
function loginUser() {
    event.preventDefault();

    var formData = {
        username: document.getElementById('usernameL').value,
        password: document.getElementById('passwordL').value,
    };

    fetch('LoginServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: formData.username,
            password: formData.password
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data == "Invalid") {
			document.getElementById("messageLE").innerText = "Username passwords do not match";
        } 
        else {
			// sets stuff to storage to use
            console.log("Login successful");
            window.location.href = 'index.html';
            sessionStorage.setItem("loggedIn", "true");
            sessionStorage.setItem("username", data.username);
            sessionStorage.setItem("name", data.name);
        }
    })
    .catch(error => {
        console.error('Error during login:', error);
        document.getElementById("messageLE").innerText = "Login failed. Please try again.";
    });
}




