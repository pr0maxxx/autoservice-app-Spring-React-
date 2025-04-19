
import React, { useEffect, useState } from 'react';
import axios from '../axiosConfig';
import './../styles/Services.css';

const Services = () => {
  const [services, setServices] = useState([]);
  const [cars, setCars] = useState([]);
  const [selectedService, setSelectedService] = useState(null);
  const [formData, setFormData] = useState({ carId: '', date: '' });
  const [successMessage, setSuccessMessage] = useState('');

  useEffect(() => {
    axios.get('/api/service-items')
      .then(res => setServices(res.data))
      .catch(err => console.error('Ошибка при загрузке услуг:', err));

    axios.get('/api/cars/my')
      .then(res => setCars(res.data))
      .catch(err => console.error('Ошибка при загрузке авто:', err));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('/api/orders', {
        serviceId: selectedService.id,
        carId: formData.carId,
        date: formData.date
      });

      setSuccessMessage("Вы успешно записались!");
      setFormData({ carId: '', date: '' });
      setSelectedService(null);
    } catch (error) {
      console.error('Ошибка при записи:', error);
    }
  };

  return (
    <div className="services-page">
      <h2>Наши услуги</h2>

      <div className="services-list">
        {services.map(service => (
          <div key={service.id} className="service-card">
            <h3>{service.name}</h3>
            <p>{service.description}</p>
            <p>Цена: {service.price} ₽</p>
            <button onClick={() => setSelectedService(service)}>Записаться</button>
          </div>
        ))}
      </div>

      {selectedService && (
        <div className="booking-form">
          <h3>Запись на: {selectedService.name}</h3>
          <form onSubmit={handleSubmit}>
            <label>Выберите автомобиль:</label>
            <select
              required
              value={formData.carId}
              onChange={(e) => setFormData({ ...formData, carId: e.target.value })}
            >
              <option value="">-- Выберите авто --</option>
              {cars.map(car => (
                <option key={car.id} value={car.id}>
                  {car.make} {car.model} ({car.licensePlate})
                </option>
              ))}
            </select>

            <label>Дата и время:</label>
            <input
              type="datetime-local"
              required
              value={formData.date}
              onChange={(e) => setFormData({ ...formData, date: e.target.value })}
            />

            <button type="submit">Записаться</button>
            <button type="button" className="cancel-btn" onClick={() => setSelectedService(null)}>Отмена</button>
          </form>
        </div>
      )}

      {successMessage && <div className="success-message">{successMessage}</div>}
    </div>
  );
};

export default Services;
