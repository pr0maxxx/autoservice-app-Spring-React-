import React, { useState, useEffect } from 'react';  // Импорт useState и useEffect
import { useNavigate } from 'react-router-dom';  // Импорт useHistory (если используете react-router)

import "./../styles/Profile.css"
import axios from './../axiosConfig';// Используем axios для работы с API

const Profile = () => {
  const [user, setUser] = useState(null);
  const [newCar, setNewCar] = useState({
    make: '',
    model: '',
    licensePlate: '',
    vin: '',
    year: ''
  });
  const [showAddCarForm, setShowAddCarForm] = useState(false);
  const isAdmin = user?.roles?.includes("ROLE_ADMIN");

  const [cars, setCars] = useState([]);
  const [appointments, setAppointments] = useState([]);
  const history = useNavigate();
  const getStatusLabel = (status) => {
    switch (status) {
      case 'NEW':
        return 'В обработке';
      case 'IN_PROGRESS':
        return 'В процессе';
      case 'WAITING_FOR_PARTS': 
        return 'Ожидание запчастей';
      case 'READY_FOR_PICKUP':
        return 'Готово к выдаче';
      case 'COMPLETED':
        return 'Завершена';
      case 'CANCELLED':
        return 'Отменена';
      default:
        return status;
    }
  };

  useEffect(() => {
    axios.defaults.headers.common['Authorization'] = `Bearer ${localStorage.getItem('token')}`;
    // Загружаем данные пользователя, автомобилей и записей
    const fetchData = async () => {
      try {
        const userResponse = await axios.get('/api/user/profile'); 
        console.log("Данные пользователя:", userResponse.data);
        setUser(userResponse.data);

        const carsResponse = await axios.get('/api/cars/my');
        setCars(carsResponse.data);

        const appointmentsResponse = await axios.get('/api/orders/my');
        setAppointments(appointmentsResponse.data);
      } catch (error) {
        console.error("Ошибка при загрузке данных:", error);
      }
    };

    fetchData();
  }, []);

  // Функция для удаления автомобиля
  const handleDeleteCar = async (carId) => {
    try {
      await axios.delete(`/api/cars/${carId}`);
      setCars(cars.filter(car => car.id !== carId)); // Обновляем список автомобилей после удаления
    } catch (error) {
      console.error("Ошибка при удалении автомобиля:", error);
    }
  };

  // Функция для редактирования автомобиля (переход на страницу редактирования)
  const handleEditCar = (carId) => {
    history(`/edit-car/${carId}`);
  };

  // Функция для отмены записи
  const handleCancelAppointment = async (appointmentId) => {
    try {
      await axios.delete(`/api/orders/${appointmentId}`);
      setAppointments(appointments.filter(app => app.id !== appointmentId)); // Обновляем список записей
    } catch (error) {
      console.error("Ошибка при отмене записи:", error);
    }
  };

  if (!user) return <div>Загрузка...</div>;

  return (
    <div className="user-profile">

      {user.role === 'ROLE_ADMIN' && (
      <section className="admin-panel">
        <h2>Админ-панель</h2>
        <button onClick={() => history('/admin/services')}>Управление услугами</button>
        <button onClick={() => history('/admin/users')}>Пользователи и их записи</button>
      </section>
      )}


      {/* Информация о пользователе */}
      <section className="user-info">
        <h2>Информация о пользователе</h2>
        <p>Имя: {user.firstName}</p>
        <p>Фамилия: {user.lastName}</p>
        <p>Логин (почта): {user.username}</p>
        <p>Телефон: {user.phone}</p>
        <button onClick={() => history('/edit-profile')}>Редактировать профиль</button>
      </section>

      {/* Мои автомобили */}
      <section className="user-cars">
      <h2>Мои автомобили</h2>

      {/* Кнопка раскрытия формы */}
      <button className="toggle-form-btn" onClick={() => setShowAddCarForm(!showAddCarForm)}>
        {showAddCarForm ? 'Скрыть форму' : 'Добавить автомобиль'}
      </button>

      {/* Форма добавления автомобиля */}
      {showAddCarForm && (
        <form className="add-car-form" onSubmit={async (e) => {
          e.preventDefault();
          try {
            await axios.post('/api/cars', newCar);
            const updatedCars = await axios.get('/api/cars/my');
            setCars(updatedCars.data);

            setNewCar({ make: '', model: '', licensePlate: '', vin: '', year: '' });
            setShowAddCarForm(false);
          } catch (error) {
            console.error("Ошибка при добавлении автомобиля:", error);
          }
        }}>
          <input type="text" placeholder="Марка" value={newCar.make} onChange={(e) => setNewCar({ ...newCar, make: e.target.value })} required />
          <input type="text" placeholder="Модель" value={newCar.model} onChange={(e) => setNewCar({ ...newCar, model: e.target.value })} required />
          <input type="text" placeholder="Номерной знак" value={newCar.licensePlate} onChange={(e) => setNewCar({ ...newCar, licensePlate: e.target.value })} required />
          <input type="text" placeholder="VIN" value={newCar.vin} onChange={(e) => setNewCar({ ...newCar, vin: e.target.value })} required/>
          <input type="number" placeholder="Год выпуска" max={new Date().getFullYear()} value={newCar.year} onChange={(e) => setNewCar({ ...newCar, year: e.target.value })}required />
          <button type="submit">Добавить</button>
        </form>
      )}

      {/* Таблица автомобилей */}
      {cars.length > 0 ? (
        <table className="car-table">
          <thead>
            <tr>
              <th>Марка</th>
              <th>Модель</th>
              <th>Номер</th>
              <th>VIN</th>
              <th>Год</th>
              <th>Действия</th>
            </tr>
          </thead>
          <tbody>
            {cars.map(car => (
              <tr key={car.id}>
                <td>{car.make}</td>
                <td>{car.model}</td>
                <td>{car.licensePlate}</td>
                <td>{car.vin}</td>
                <td>{car.year}</td>
                <td>
                  <button onClick={() => handleEditCar(car.id)}>Редактировать</button>
                  <button className="delete-btn" onClick={() => handleDeleteCar(car.id)}>Удалить</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>У вас нет автомобилей.</p>
      )}
    </section>



    {/* Мои записи на услуги */}
    <section className="user-appointments">
    <h2>Мои записи на услуги</h2>
    {appointments.length > 0 ? (
      <table>
        <thead>
          <tr>
            <th>Автомобиль</th>
            <th>Услуга</th>
            <th>Дата и время</th>
            <th>Статус</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          {appointments.map(app => (
            <tr key={app.id}
            className={app.status === 'COMPLETED' || app.status === 'CANCELLED' ? 'dimmed' : ''}>
              <td>{app.car.make} {app.car.model}</td>
              <td>{app.service?.name || 'Неизвестная услуга'}</td> {/* Отображаем название услуги */}
              <td>{new Date(app.date).toLocaleString()}</td>
              <td>{getStatusLabel(app.status)}</td>
              <td>
                  {app.status !== 'COMPLETED' && app.status !== 'CANCELLED' && (
                    <button onClick={() => handleCancelAppointment(app.id)}>Отменить запись</button>
                  )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    ) : (
      <p>У вас нет записей. <button onClick={() => history('/services')}>Создать новую запись</button></p>
    )}
  </section>

    </div>
  );
};

export default Profile;
