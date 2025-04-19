import React from "react";
import "./../styles/About.css";

const About = () => {
  return (
    <div className="aboutus-container">
      <div className="aboutus-content">
        <h1 className="aboutus-title">О нас</h1>
        <p className="aboutus-paragraph">
          Добро пожаловать в <span className="highlight">AutoMaster</span> — современный автосервис, где качество и забота о клиенте стоят на первом месте. Мы работаем для того, чтобы вы чувствовали себя уверенно на дороге, доверяя обслуживание своего автомобиля профессионалам.
        </p>
        <p className="aboutus-paragraph">
          Наша команда — это квалифицированные механики с многолетним опытом, которые используют только современное оборудование и проверенные запчасти. Мы предлагаем широкий спектр услуг — от планового техобслуживания до сложного ремонта.
        </p>
        <p className="aboutus-paragraph">
          Мы ценим ваше время и комфорт, поэтому предлагаем удобную систему онлайн-записи, прозрачные цены и честную диагностику. 
        </p>
        <p className="aboutus-paragraph">
          AutoMaster — это не просто автосервис, это сервис, которому можно доверять.
        </p>

        <div className="aboutus-stats">
          <div>
            <h2 className="stat-title">10+ лет опыта</h2>
            <p>в сфере автосервиса</p>
          </div>
          <div>
            <h2 className="stat-title">5000+ клиентов</h2>
            <p>доверили нам свои автомобили</p>
          </div>
          <div>
            <h2 className="stat-title">Гарантия до 12 мес</h2>
            <p>на выполненные работы</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default About;
