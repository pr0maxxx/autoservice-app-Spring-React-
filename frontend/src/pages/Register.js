
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './../styles/Login.css';

const Register = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    firstName: '',
    lastName: '',
    phone: '',
    email: '',
  });

  const [error, setError] = useState('');

  const handleChange = (e) => {
    setFormData(prev => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch('/api/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        throw new Error('Ошибка при регистрации');
      }

      navigate('/login');
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="auth-container">
      <form className="auth-form" onSubmit={handleSubmit}>
        <h2>Регистрация</h2>

        <label>Почта(логин)</label>
        <input
          type="text"
          name="username"
          placeholder="Введите почту"
          value={formData.username}
          onChange={handleChange}
          required
        />

        <label>Пароль</label>
        <input
          type="password"
          name="password"
          placeholder="Введите пароль"
          value={formData.password}
          onChange={handleChange}
          required
        />

        <label>Имя</label>
        <input
          type="text"
          name="firstName"
          placeholder="Введите имя"
          value={formData.firstName}
          onChange={handleChange}
          required
        />

        <label>Фамилия</label>
        <input
          type="text"
          name="lastName"
          placeholder="Введите фамилию"
          value={formData.lastName}
          onChange={handleChange}
          required
        />

        <label>Телефон</label>
        <input
          type="tel"
          name="phone"
          placeholder="Введите телефон"
          value={formData.phone}
          onChange={handleChange}
          required
        />

        <button type="submit">Зарегистрироваться</button>
        {error && <p className="error">{error}</p>}
      </form>
    </div>
  );
};

export default Register;
