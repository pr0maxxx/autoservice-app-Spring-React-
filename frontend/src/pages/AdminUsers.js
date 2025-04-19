import React, { useEffect, useState } from 'react';
import axios from '../axiosConfig';
import './../styles/AdminUsers.css';

const AdminUsers = () => {
  const [users, setUsers] = useState([]);
  const [expandedUserId, setExpandedUserId] = useState(null);

  const statusOptions = {
    NEW: 'В обработке',
    WAITING_FOR_PARTS: 'Ожидание запчастей',
    IN_PROGRESS: 'В процессе',
    READY_FOR_PICKUP: 'Готова к выдаче',
    COMPLETED: 'Завершена',
    CANCELLED: 'Отменена',
  };

  useEffect(() => {
    axios.get('/api/admin/users')
      .then(res => setUsers(res.data))
      .catch(err => console.error("Ошибка при загрузке пользователей:", err));
  }, []);

  const toggleUser = (userId) => {
    setExpandedUserId(prevId => prevId === userId ? null : userId);
  };

  return (
    <div className="admin-users">
      <h2>Пользователи</h2>
      {users.map(user => (
        <div key={user.id} className="user-card">
          <div className="user-header" onClick={() => toggleUser(user.id)}>
            <h3>{user.firstName} {user.lastName} ({user.username})</h3>
            <p>Телефон: {user.phone}</p>
          </div>
          {expandedUserId === user.id && (
            <div className="user-details">
              <UserCars userId={user.id} />
              <UserAppointments userId={user.id} />
            </div>
          )}
        </div>
      ))}
    </div>
  );
};

const UserCars = ({ userId }) => {
  const [cars, setCars] = useState([]);

  useEffect(() => {
    axios.get(`/api/admin/users/${userId}/cars`)
      .then(res => setCars(res.data))
      .catch(err => console.error("Ошибка загрузки авто:", err));
  }, [userId]);

  return (
    <div className="user-cars">
      <h4>Автомобили:</h4>
      {cars.length > 0 ? (
        <table className="admin-table">
          <thead>
            <tr>
              <th>Марка</th>
              <th>Модель</th>
              <th>Номер</th>
            </tr>
          </thead>
          <tbody>
            {cars.map(car => (
              <tr key={car.id}>
                <td>{car.make}</td>
                <td>{car.model}</td>
                <td>{car.licensePlate}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>Нет автомобилей</p>
      )}
    </div>
  );
};

const UserAppointments = ({ userId }) => {
  const [appointments, setAppointments] = useState([]);

  const statusOptions = {
    NEW: 'В обработке',
    IN_PROGRESS: 'В процессе',
    WAITING_FOR_PARTS: 'Ожидание запчастей',
    READY_FOR_PICKUP: 'Готова к выдаче',
    COMPLETED: 'Завершена',
    CANCELLED: 'Отменена',
  };

  useEffect(() => {
    axios.get(`/api/orders/client/${userId}`)
      .then(res => setAppointments(res.data))
      .catch(err => console.error("Ошибка загрузки записей:", err));
  }, [userId]);

  const token = localStorage.getItem('token');

  const handleStatusChange = async (orderId, newStatus) => {
    try {
      const response = await fetch(`/api/orders/${orderId}/status?status=${newStatus}`, {
        method: 'PUT',
        headers: {
          Authorization: `Bearer ${token}`,
        }
      });
  
      if (response.ok) {
        const updatedOrder = await response.json(); // Получаем обновлённый заказ с новым статусом
  
        setAppointments(prev =>
          prev.map(app =>
            app.id === orderId ? { ...app, status: updatedOrder.status } : app
          )
        );
      } else {
        console.error('Ошибка при обновлении статуса. Код:', response.status);
      }
    } catch (error) {
      console.error('Ошибка при обновлении статуса:', error);
    }
  };
  

  return (
    <div className="user-appointments">
      <h4>Записи:</h4>
      {appointments.length > 0 ? (
        <table className="admin-table">
          <thead>
            <tr>
              <th>Автомобиль</th>
              <th>Услуга</th>
              <th>Дата</th>
              <th>Статус</th>
            </tr>
          </thead>
          <tbody>
            {appointments.map(app => (
              <tr key={app.id}>
                <td>{app.car.make} {app.car.model}</td>
                <td>{app.service.name}</td>
                <td>{new Date(app.date).toLocaleString()}</td>
                <td>
                  <select
                    value={app.status}
                    onChange={(e) => handleStatusChange(app.id, e.target.value)}
                  >
                    {Object.entries(statusOptions).map(([value, label]) => (
                      <option key={value} value={value}>{label}</option>
                    ))}
                  </select>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>Нет записей</p>
      )}
    </div>
  );
};


export default AdminUsers;
