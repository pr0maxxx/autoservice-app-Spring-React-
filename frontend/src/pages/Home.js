import React from 'react';
import { Link } from 'react-router-dom';
import './../styles/Home.css';

const Home = () => {
  return (
    <div className="home">
      {/* Заголовок */}
      <header className="home-header">
        <h1 className="title">Автосервис "AutoPro"</h1>
        <p className="subtitle">Надежное обслуживание вашего автомобиля</p>
        <Link to="/login" className="home-button">Записаться на обслуживание</Link>
      </header>

      {/* О нас */}
      <section className="home-section about-us">
        <h2>О нас</h2>
        <p>
          Мы — команда профессионалов с более чем 10-летним опытом в обслуживании и ремонте автомобилей.
          Предлагаем качественный сервис, прозрачные цены и индивидуальный подход к каждому клиенту.
        </p>
      </section>

      {/* Услуги */}
      <section className="home-section services-section">
        <h2>Популярные услуги</h2>
        <div className="services">
          <div className="service-card">
            <h3>Замена масла</h3>
            <p>Быстрая и качественная замена масла и фильтра</p>
          </div>
          <div className="service-card">
            <h3>Ротация шин</h3>
            <p>Продлите срок службы шин с помощью регулярной ротации</p>
          </div>
          <div className="service-card">
            <h3>Проверка тормозов</h3>
            <p>Диагностика тормозной системы для вашей безопасности</p>
          </div>
        </div>
      </section>


      {/* Футер */}
      <footer className="footer">
        <div className="footer-content">
          <p>&copy; 2025 Автосервис "AutoPro". Все права защищены.</p>
          <div className="footer-links">
            <Link to="/about" className="footer-link">О нас</Link>
            <Link to="/services" className="footer-link">Услуги</Link>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default Home;
