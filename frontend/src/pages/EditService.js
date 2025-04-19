import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from './../axiosConfig';

const EditService = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [service, setService] = useState(null);

  useEffect(() => {
    axios.get(`/api/service-items/${id}`)
      .then(res => setService(res.data))
      .catch(err => console.error("Ошибка загрузки услуги:", err));
  }, [id]);

  const handleUpdate = async (e) => {
    e.preventDefault();
    try {
      await axios.put(`/api/service-items/${id}`, service);
      navigate('/admin/services');
    } catch (error) {
      console.error("Ошибка при обновлении:", error);
    }
  };

  if (!service) return <div>Загрузка...</div>;

  return (
    <div className="edit-service">
      <h2>Редактировать услугу</h2>
      <form onSubmit={handleUpdate}>
        <input
          type="text"
          placeholder="Название"
          value={service.name}
          onChange={(e) => setService({ ...service, name: e.target.value })}
          required
        />
        <textarea
          placeholder="Описание"
          value={service.description}
          onChange={(e) => setService({ ...service, description: e.target.value })}
          required
        />
        <input
          type="number"
          placeholder="Цена"
          value={service.price}
          onChange={(e) => setService({ ...service, price: e.target.value })}
          required
        />
        <button type="submit">Сохранить</button>
        <button type="button" onClick={() => navigate('/admin/services')}>Отмена</button>
      </form>
    </div>
  );
};

export default EditService;
