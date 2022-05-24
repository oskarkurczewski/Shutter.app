import jwtDecode from "jwt-decode";

const decodeToken = (token) => {
   const decoded = jwtDecode(token || "");
   const decodedJson = JSON.parse(JSON.stringify(decoded));
   const roles = decodedJson.roles.split(",");
   const username = decodedJson.sub;

   const currentRole = localStorage.getItem("accessLevel");
   let accessLevel;
   if (currentRole && roles.includes(currentRole)) {
      accessLevel = currentRole;
   } else {
      accessLevel = roles[0];
   }

   const exp = decodedJson.exp * 1000;

   return { username, roles, accessLevel, token, exp };
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
