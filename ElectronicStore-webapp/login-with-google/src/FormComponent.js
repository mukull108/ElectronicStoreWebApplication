import { GoogleLogin } from '@react-oauth/google';
import axios from 'axios';

function FormComponent() {

    const handleLoginSuccess = (response) => {
        const token = response.credential;  // This is the Google JWT token
        console.log('Google Token:', token);

        // Send the token to the Spring Boot backend for verification
        axios.post('http://localhost:8080/auth/login-with-google', {
            token: token,  // sending the token in the body
        })
            .then(response => {
                console.log('Login successful:', response.data);
            })
            .catch(error => {
                console.error('Error during login:', error);
            });
    };

    return (
        <div>
            {/* Google One Tap Login */}
            <GoogleLogin
                onSuccess={handleLoginSuccess}
                onError={() => console.log('Login Failed')}
            />
        </div>
    );
}

export default FormComponent;
