import React, { useEffect, useState } from 'react';
import axios from './../axiosConfig';
import './../styles/ServiceManagement.css';
import { useNavigate } from 'react-router-dom';

const ServiceManagement = () => {
  const [services, setServices] = useState([]);
  const [showAddForm, setShowAddForm] = useState(false);
  const [newService, setNewService] = useState({
    name: '',
    description: '',
    price: ''
  });

  useEffect(() => {
    fetchServices();
  }, []);

  const fetchServices = async () => {
    try {
      const response = await axios.get('/api/service-items');
      setServices(response.data);
    } catch (err) {
      console.error('Ошибка при загрузке услуг:', err);
    }
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`/api/service-items/${id}`);
      setServices(services.filter(service => service.id !== id));
    } catch (err) {
      console.error('Ошибка при удалении услуги:', err);
    }
  };

  const handleAddService = async (e) => {
    e.preventDefault();
    try {
      await axios.post('/api/service-items', newService);
      await fetchServices();
      setNewService({ name: '', description: '', price: '' });
      setShowAddForm(false);
    } catch (err) {
      console.error('Ошибка при добавлении услуги:', err);
    }
  };
  const navigate = useNavigate();

  const handleEditService = (id) => {
    navigate(`/edit-service/${id}`);
  };

  return (
    <div className="service-management">
      <h2>Управление услугами</h2>

      <button className="toggle-form-btn" onClick={() => setShowAddForm(!showAddForm)}>
        {showAddForm ? 'Скрыть форму' : 'Добавить услугу'}
      </button>

      {showAddForm && (
        <form className="add-service-form" onSubmit={handleAddService}>
          <input
            type="text"
            placeholder="Название"
            value={newService.name}
            onChange={(e) => setNewService({ ...newService, name: e.target.value })}
            required
          />
          <input
            type="text"
            placeholder="Описание"
            value={newService.description}
            onChange={(e) => setNewService({ ...newService, description: e.target.value })}
            required
          />
          <input
            type="number"
            placeholder="Цена"
            value={newService.price}
            onChange={(e) => setNewService({ ...newService, price: e.target.value })}
            required
          />
          <button type="submit">Добавить</button>
        </form>
      )}

      <table className="service-table">
        <thead>
          <tr>
            <th>Название</th>
            <th>Описание</th>
            <th>Цена</th>
            <th>Действия</th>
          </tr>
        </thead>
        <tbody>
          {services.map(service => (
            <tr key={service.id}>
              <td>{service.name}</td>
              <td>{service.description}</td>
              <td>{service.price} ₽</td>
              <td>
                <button onClick={() => handleEditService(service.id)}>Редактировать</button>
                <button className="delete-btn" onClick={() => handleDelete(service.id)}>Удалить</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ServiceManagement;
