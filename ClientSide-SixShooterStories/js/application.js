function showLoginForm() {
    templateBuilder.build('login-form', {}, 'login');
}

function showRegisterForm() {
    templateBuilder.build('register', {}, 'register');
}

function hideModalForm() {
    templateBuilder.clear('login');
}

function hideRegisterForm() {
    templateBuilder.clear('register');
}

function login() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    userService.login(username, password);
    hideModalForm();
}

function loadHome() {
    templateBuilder.build('home',{},'main')
}

function editProfile() {
    profileService.loadProfile();
}

function saveProfile()
{
    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const email = document.getElementById("email").value;
    const city = document.getElementById("city").value;
    const state = document.getElementById("state").value;
    const github = document.getElementById("github").value;
    // Maybe want date registered here

    const profile = {
        firstName,
        lastName,
        email,
        github,
        city,
        state
    };

    profileService.updateProfile(profile);
}

function closeError(control)
{
    setTimeout(() => {
        control.click();
    },3000);
}

document.addEventListener('DOMContentLoaded', () => {

    loadHome();
});