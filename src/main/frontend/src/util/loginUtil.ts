import jwtDecode from "jwt-decode";

const decodeToken = (token) => {
   const decoded = jwtDecode(token || "");
   const decodedJson = JSON.parse(JSON.stringify(decoded));
   const roles = decodedJson.roles.split(",");
   const username = decodedJson.sub;
   const accessLevel = roles[0];
   console.log(username, roles, accessLevel, token);

   return { username, roles, accessLevel, token };
};

export const getLoginPayload = () => {
   const token = localStorage.getItem("token");
   return decodeToken(token);
};

export const getTokenExp = () => {
   const decoded = jwtDecode(localStorage.getItem("token") || "");

   const decodedJson = JSON.parse(JSON.stringify(decoded));
   const exp = decodedJson.exp * 1000;

   return exp;
};

export const refreshToken = (token: string) => {
   localStorage.setItem("token", token);
   return decodeToken(token);
};
