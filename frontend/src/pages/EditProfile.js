import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from './../axiosConfig';
import "./../styles/EditProfile.css";

const EditProfile = () => {
  const [user, setUser] = useState({
    firstName: '',
    lastName: '',
    phone: '',
    username: '',
    password: ''
  });

  const navigate = useNavigate();

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await axios.get('/api/user/profile');
        setUser({ ...response.data, password: '' }); // не показываем текущий пароль
      } catch (error) {
        console.error("Ошибка загрузки профиля:", error);
      }
    };

    fetchProfile();
  }, []);

  const handleChange = (e) => {
    setUser(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.put('/api/user/profile', user);
      navigate('/profile');
    } catch (error) {
      console.error("Ошибка при обновлении профиля:", error);
      alert("Не удалось обновить профиль");
    }
  };

  return (
    <div className="edit-profile">
      <h2>Редактировать профиль</h2>
      <form onSubmit={handleSubmit}>
        <label>Имя:
          <input type="text" name="firstName" value={user.firstName} onChange={handleChange} required />
        </label>
        <label>Фамилия:
          <input type="text" name="lastName" value={user.lastName} onChange={handleChange} required />
        </label>
        <label>Телефон:
          <input type="tel" name="phone" value={user.phone} onChange={handleChange} required />
        </label>
        <label>Новый пароль (опционально):
          <input type="password" name="password" value={user.password} onChange={handleChange} />
        </label>
        <button type="submit">Сохранить</button>
      </form>
    </div>
  );
};

export default EditProfile;
