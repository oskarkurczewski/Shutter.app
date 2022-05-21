import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import "./style.scss";

import LoginPage from "pages/login";
import DashboardPage from "pages/dashboard";
import AuthenticatedRoute from "util/AuthenticatedRoute";
import Confirm from "components/client/Confirm";
import { useAppDispatch } from "redux/hooks";
import { getLoginPayload } from "util/loginUtil";
import { login } from "redux/slices/authSlice";

function App() {
   const dispatch = useAppDispatch();
   if (localStorage.getItem("token")) {
      const payload = getLoginPayload();
      dispatch(login(payload));
   }

   return (
      <BrowserRouter>
         <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/confirm/:registerationToken" element={<Confirm />} />
            <Route
               path="/dashboard"
               element={
                  <AuthenticatedRoute>
                     <DashboardPage />
                  </AuthenticatedRoute>
               }
            />
         </Routes>
      </BrowserRouter>
   );
}

export const apiUrl = "http://studapp.it.p.lodz.pl:8002/api";

export default App;
