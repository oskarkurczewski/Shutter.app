import React, { useState } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import "./style.scss";

import LoginPage from "pages/login";
import DashboardPage from "pages/dashboard";
import AuthenticatedRoute from "util/AuthenticatedRoute";

function App() {
   const [token, setToken] = useState<string>("");
   return (
      <BrowserRouter>
         <Routes>
            <Route path="/login" element={<LoginPage setToken={setToken} />} />
            <Route
               path="/dashboard"
               element={
                  <AuthenticatedRoute token={token}>
                     <DashboardPage token={token} setToken={setToken} />
                  </AuthenticatedRoute>
               }
            />
         </Routes>
      </BrowserRouter>
   );
}

export default App;
