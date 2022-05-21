import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import "./style.scss";

import LoginPage from "pages/login";
import DashboardPage from "pages/dashboard";
import AuthenticatedRoute from "util/AuthenticatedRoute";
import Confirm from "components/client/Confirm";
import PageLayout from "pages/layout";
import Homepage from "pages/homepage";
import NotFound404 from "pages/not-found";
import { useAppDispatch } from "redux/hooks";
import { getLoginPayload, getTokenExp } from "util/loginUtil";
import { login } from "redux/slices/authSlice";

function App() {
   const dispatch = useAppDispatch();
   if (localStorage.getItem("token") && Date.now() < getTokenExp()) {
       dispatch(login(getLoginPayload()));
   } else {
       localStorage.setItem("accessLevel", "GUEST");
   }

   return (
      <BrowserRouter>
         <Routes>
            <Route path="*" element={<NotFound404 />} />
            <Route element={<PageLayout />}>
               <Route path="/" element={<Homepage />} />
               <Route path="/login" element={<LoginPage />} />
               <Route
                  path="/dashboard"
                  element={
                     <AuthenticatedRoute>
                        <DashboardPage />
                     </AuthenticatedRoute>
                  }
               />
               <Route path="/confirm/:registerationToken" element={<Confirm />} />
            </Route>
         </Routes>
      </BrowserRouter>
   );
}

export const apiUrl = "http://studapp.it.p.lodz.pl:8002/api";

export default App;
