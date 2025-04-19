import "./../styles/Header.css";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from './../context/AuthContext';  // Импортируем контекст
import { useEffect } from "react";

const Header = () => {
  const { user, logout } = useAuth();  // Используем данные из контекста
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();  // Вызываем logout из контекста
    localStorage.removeItem("token");  // Убираем токен из localStorage
    navigate("/");  // Перенаправляем на главную
  };

  return (
    <header className="header">
      <div className="header-left">
        <Link to="/" className="logo">AutoService</Link>
        <Link to="/" className="nav-link">Главная</Link>
        <Link to="/services" className="nav-link">Услуги</Link>
        <Link to="/about" className="nav-link">О нас</Link>
      </div>

      <div className="header-right">
        {user ? (  // Используем user из контекста вместо состояния
          <>
            <Link to="/profile" className="nav-link">Личный кабинет</Link>
            <button onClick={handleLogout} className="logout-button">Выйти</button>
          </>
        ) : (
          <>
            <Link to="/login" className="nav-link">Вход</Link>
            <Link to="/register" className="nav-link">Регистрация</Link>
          </>
        )}
      </div>
    </header>
  );
};

export default Header;
