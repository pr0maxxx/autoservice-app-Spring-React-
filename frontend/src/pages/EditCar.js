import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import "./../styles/EditCar.css";

const EditCar = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [car, setCar] = useState(null);

  useEffect(() => {
    axios.defaults.headers.common['Authorization'] = `Bearer ${localStorage.getItem('token')}`;

    const fetchCar = async () => {
      try {
        const response = await axios.get(`/api/cars/${id}`);
        setCar(response.data);
      } catch (error) {
        console.error("Ошибка при загрузке данных автомобиля:", error);
      }
    };

    fetchCar();
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCar(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.put(`/api/cars/${id}`, car);
      navigate("/profile");
    } catch (error) {
      console.error("Ошибка при обновлении автомобиля:", error);
    }
  };

  if (!car) return <div>Загрузка...</div>;

  return (
    <div className="edit-car-container">
      <h2>Редактировать автомобиль</h2>
      <form onSubmit={handleSubmit}>
        <input type="text" name="make" value={car.make} onChange={handleChange} placeholder="Марка" required />
        <input type="text" name="model" value={car.model} onChange={handleChange} placeholder="Модель" required />
        <input type="text" name="licensePlate" value={car.licensePlate} onChange={handleChange} placeholder="Номерной знак" required />
        <input type="text" name="vin" value={car.vin} onChange={handleChange} placeholder="VIN"required />
        <input type="number" name="year" value={car.year} onChange={handleChange} placeholder="Год выпуска" max={new Date().getFullYear()} required/>
        <button type="submit">Сохранить изменения</button>
      </form>
    </div>
  );
};

export default EditCar;
