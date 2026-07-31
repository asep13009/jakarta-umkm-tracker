CREATE TABLE umkm (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255),
  category VARCHAR(100),
  kecamatan VARCHAR(100),
  lat DOUBLE PRECISION,
  lng DOUBLE PRECISION,
  rating FLOAT
);

CREATE TABLE orders (
  id SERIAL PRIMARY KEY,
  umkm_id INT REFERENCES umkm(id),
  product_name VARCHAR(100),
  amount INT,
  order_time TIMESTAMP DEFAULT NOW()
);

INSERT INTO umkm (name, category, kecamatan, lat, lng, rating) VALUES
('Bakso Pakde Kemang', 'Makanan', 'Jakarta Selatan', -6.261, 106.806, 4.7),
('Kopi Kulo Thamrin', 'Minuman', 'Jakarta Pusat', -6.195, 106.823, 4.5);