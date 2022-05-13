import jwtDecode from "jwt-decode";

export const getLoginPayload = () => {
   const decoded = jwtDecode(localStorage.getItem("token") || "");
   const decodedJson = JSON.parse(JSON.stringify(decoded));
   const roles = decodedJson.roles.split(",");
   const name = decodedJson.sub;

   return { username: name, accessLevel: roles };
};
