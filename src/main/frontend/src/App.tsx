import React, { useState } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import "./style.scss";

import LoginPage from "pages/login";
import DashboardPage from "pages/dashboard";

function App() {
   const [token, setToken] = useState<string>("");
   return (
      <BrowserRouter>
         <Routes>
            <Route path="/login" element={<LoginPage setToken={setToken} />} />
            <Route path="/dashboard" element={<DashboardPage token={token} />} />
         </Routes>
      </BrowserRouter>
   );
}

export default App;
