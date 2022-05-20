import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import "./style.scss";

import LoginPage from "pages/login";
import DashboardPage from "pages/dashboard";
import AuthenticatedRoute from "util/AuthenticatedRoute";
import Confirm from "components/client/Confirm";

function App() {
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
