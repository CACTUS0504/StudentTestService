import { useLocalState } from "./useLocalState";

function logout() {
    localStorage.setItem("jwt", "");
    window.location.href = "/login"
}

export default logout;