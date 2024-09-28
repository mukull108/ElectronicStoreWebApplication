import './App.css';
import { GoogleOAuthProvider} from '@react-oauth/google';
import FormComponent from './FormComponent';


function App() {
  return (
    <div className='flex items-center flex-col justify-center h-screen'>

      <h1 className="font-bold text-3xl mb-5">Welcome to Login with Google App</h1>
      <GoogleOAuthProvider clientId="180835735572-t6qef0l27uhtlmps5dfujiq35biaqg57.apps.googleusercontent.com">
        <FormComponent />
      </GoogleOAuthProvider>

    </div>
  );
}

export default App;
