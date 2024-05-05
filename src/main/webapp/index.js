var socket;

document.addEventListener('DOMContentLoaded', function() {
	// shows right buttons
    updateUIBasedOnLoginStatus();
    createSessionForUser();
    setTimeout(function() {
    	socket.send("Signup");
    }, 500);
});

function updateUIBasedOnLoginStatus() {
    const loggedIn = sessionStorage.getItem("loggedIn") === "true";
    const greetingElement = document.getElementById("greeting");
    const loginButton = document.getElementById("loginsignup-button");
    const addButton = document.getElementById("addEventButton"); // Assuming this exists
    const logoutButton = document.getElementById("logoutButton");
    const deleteAccountButton = document.getElementById("deleteAccountButton");
    
    // hide and show the buttons based of if loggein OR NOT
    if (loggedIn) {
        const name = sessionStorage.getItem("name");
        greetingElement.innerText = `Hello ${name}`;
        loginButton.style.display = 'none';
        addButton.style.display = 'block';
        logoutButton.style.display = 'block';
        deleteAccountButton.style.display = 'block';
    } else {
        greetingElement.innerText = "";
        loginButton.style.display = 'block';
        addButton.style.display = 'none';
        logoutButton.style.display = 'none';
        deleteAccountButton.style.display = 'none';
    }
}

function logoutUser() {
	// removes the sessionstorage stuff
    sessionStorage.removeItem("loggedIn");
    sessionStorage.removeItem("username");
    sessionStorage.removeItem("name");
    updateUIBasedOnLoginStatus(); // hides and displays correct buttons
    window.location.href = 'index.html';
}


// function to delete user
// will delete from tables and then log them out and go to home pagse
function deleteUser() {
	
    if (confirm("Are you sure you want to delete your account? This action cannot be undone.")) {
        fetch('DeleteUserServlet', {
	        method: 'POST',
	        headers: {
	            'Content-Type': 'application/json'
	        },
	        body: JSON.stringify({
	            username: sessionStorage.getItem('username'),
	        })
    	})
    	.then(response => response.json())
        .then(data => {
			console.log(data);
            if (data == "Successful") {
                alert("Account deleted successfully");
                window.location.href = "index.html"; // go back to home page
                logoutUser();
                socket.send("Delete");
                
            } else {
                alert("Failed to delete account.");
            }
        })
        .catch(error => {
            console.error('Error deleting account:', error);
            alert("Error during account deletion.");
        });
    }
    
}


// siplays correct events in calendar
$(document).ready(function() {
	setTimeout(function() {
	    $('#calendar').fullCalendar({
	        header: {
	            left: 'prev,next today',
	            center: 'title',
	            right: ''
	        },
	        editable: true,
	        eventLimit: true, // allow "more" link when too many events
	
	        // Fetch and display events
	        events: function(start, end, timezone, callback) {
				    var selectedUsers = $('.user-filter:checked').map(function() {
	                return $(this).val(); // get the value from each checked checkbox
	            }).get(); // TO CONVERT the jQuery object into a regular array we can use
	            
	            console.log("seleted users: ");
	            console.log(selectedUsers);	
	            
	            $.ajax({
	                url: 'FetchEventsServlet',
	                type: 'GET',
	                data: { users: JSON.stringify(selectedUsers) },
	                dataType: 'json',
	                success: function(response) {
	                    var events = [];
	                    $(response).each(function() {
	                        events.push({
	                            title: this.eventName,
	                            start: this.eventDate + 'T' + this.startTime,
	                            end: this.eventDate + 'T' + this.endTime,
	                            description: this.description
	                        });
	                    });
	                    callback(events);
	                }
	            });            
	            
	        }
	    });
	}, 900);
    // fetch and display users with checkbox filters
    fetchAndDisplayUsers();
});




// to check wihc users to show
function fetchAndDisplayUsers() {
    $.ajax({
        url: 'FetchUsersServlet',
        type: 'GET',
        dataType: 'json',
        success: function(users) {
            var filters = users.map(function(user) {
                return `<label><input type="checkbox" class="user-filter" value="${user.userID}" checked> ${user.name}</label><br>`;
            }).join('');
            $('#userFilters').empty().append(filters);
            updateUserFilterListeners();
        }
    });
}

function updateUserFilterListeners() {
    $('.user-filter').change(function() {
        $('#calendar').fullCalendar('refetchEvents');
    });
}



document.addEventListener('DOMContentLoaded', function() {
    var addEventButton = document.getElementById('addEventButton');
    if (addEventButton) {
        addEventButton.addEventListener('click', showAddEventModal);
    }

    // to close modal when the close x button is pressed
    var closeButton = document.querySelector('.close');
    if (closeButton) {
        closeButton.addEventListener('click', closeAddEventModal);
    }

    // to Close the modal when clicking outside of it
    window.onclick = function(event) {
        var modal = document.getElementById('addEventModal');
        if (event.target == modal) {
            modal.style.display = 'none';
        }
    }
});

// function to make the modal window pop up
function showAddEventModal() {
    var modal = document.getElementById('addEventModal');
    modal.style.display = 'block';
}

// function to hide the modal when the close button is clicked
function closeAddEventModal() {
    var modal = document.getElementById('addEventModal');
    modal.style.display = 'none';
}


// ADDING VCENT TO CLANEDAR
function addEvent() {
	// Prevent default
    event.preventDefault();
    
    var username = sessionStorage.getItem('username');
    
    var eventData = {
        eName: document.getElementById('eventName').value,
        eDate: document.getElementById('eventDate').value,
        eStartTime: document.getElementById('eventStartTime').value + ":00",
        eEndTime: document.getElementById('eventEndTime').value + ":00",
        eLocation: document.getElementById('eventLocation').value,
        eDescription: document.getElementById('eventDescription').value,
        username: username
    };
    

    fetch('AddEventServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            eventName: eventData.eName,
            eventDate: eventData.eDate,
            startTime: eventData.eStartTime,
            endTime: eventData.eEndTime,
            eventLocation: eventData.eLocation,
            description: eventData.eDescription,
            username: eventData.username
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data == "EventAddFail") {
			console.error("Failed to add event");
        }
        else {
            console.log("Event added successfully");
            // Close the modal
            document.getElementById('addEventModal').style.display = 'none';
            socket.send("Update calendars");
        }
    })
    .catch(error => {
        console.error('Error adding event:', error);
    });
}

function createSessionForUser(){
	socket = new WebSocket("ws://localhost:9090/AllInOneCalendar/websocket");
	
	socket.onopen = function(event) {
        console.log("WebSocket connection opened");
    };

    socket.onmessage = function(event) {
		console.log("Received message");
		console.log(event.data);
  		$('#calendar').fullCalendar('refetchEvents');
  		if(event.data != "Update calendars"){
			  fetchAndDisplayUsers();
		}	
    };

    socket.onclose = function(event) {		
		console.log("WebSocket connection closed");
    };

    socket.onerror = function(error) {
		console.log("WebSocket connection error");
    };

}
