import { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import axios from 'axios';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, PieChart, Pie, Cell, LineChart, Line } from 'recharts';
import { Search, MapPin, TrendingUp, ShoppingCart, DollarSign } from 'lucide-react';

const COLORS = ['#4f46e5', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6'];

export default function Dashboard() {
  // State Dashboard
    const [mapData, setMapData] = useState([
    { name: 'Kecamatan A', sales: 1200000 },
    { name: 'Kecamatan B', sales: 900000 },
    { name: 'Kecamatan C', sales: 1500000 },
  ]);
  const [summary, setSummary] = useState({ totalSalesToday: 3600000,totalOrdersToday:1233, avgOrderValue:20000 });
  const [topUmkm, setTopUmkm] = useState([
    { umkmName: 'UMKM Contoh 1 ', totalSales: 1200000 },
    { umkmName: 'UMKM Contoh 2 ', totalSales: 900000 },
    { umkmName: 'UMKM Contoh 3 ', totalSales: 700000 },
  ]); 

  const [recentOrders, setRecentOrders] = useState([
    { umkmName: 'UMKM Contoh 1 ', productName: 'Baso', amount: '200000' },
    { umkmName: 'UMKM Contoh 2 ', productName: 'Es Kelapa', amount: '500000' },
    { umkmName: 'UMKM Contoh 3 ', productName: 'Nasi Goreng', amount: '240000' },
  ]);


const [trend24h, setTrend24h] = useState([
  { hour: "00:00", total: 120000 },
  { hour: "02:00", total: 180000 },
  { hour: "04:00", total: 150000 },
  { hour: "06:00", total: 250000 },
  { hour: "08:00", total: 450000 },
  { hour: "10:00", total: 720000 },
  { hour: "12:00", total: 980000 },
  { hour: "14:00", total: 1250000 },
  { hour: "16:00", total: 1400000 },
  { hour: "18:00", total: 1650000 },
  { hour: "20:00", total: 1350000 },
  { hour: "22:00", total: 850000 },
]);
  // State Search
  const [query, setQuery] = useState("");
  const [searchResults, setSearchResults] = useState([
      // {umkmId:1, umkmName: 'UMKM Contoh 1 ', productName: 'Baso', amount: '200000', kecamatan:'Jakarta Selatan' },
      // {umkmId:2, umkmName: 'UMKM Contoh 2 ', productName: 'Es Kelapa', amount: '500000', kecamatan:'Jakarta Selatan' },
      // {umkmId:3, umkmName: 'UMKM Contoh 3 ', productName: 'Nasi Goreng', amount: '240000', kecamatan:'Jakarta Selatan' },
  ]);
  const [selectedKecamatan, setSelectedKecamatan] = useState("");

  const [error, setError] = useState('');

  useEffect(() => {
    loadInitialData();
    // try{
      const client = new Client({
        webSocketFactory: () => new SockJS('http://localhost:8083/ws-dashboard'),
        // webSocketFactory: () => new SockJS('/ws-dashboard'),
        reconnectDelay: 5000,
        onConnect: () => {
          client.subscribe('/topic/realtime-map', (msg) => {
              const rawObject = JSON.parse(msg.body); // Dapat: {"Jakarta Barat": 40256, ...}
              const formattedArray = Object.keys(rawObject).map((key) => ({
                name: key,          // Kecamatan
                sales: rawObject[key] // Nilai total sales
              }));
              setMapData(formattedArray); 
            });
          client.subscribe('/topic/summary', (msg) => setSummary(JSON.parse(msg.body)));
          client.subscribe('/topic/recent-orders', (msg) => setRecentOrders(JSON.parse(msg.body)));
        }
      });
      client.activate();
      return () => client.deactivate();

    // } catch (err) {
    //     setError('Backend belum siap, menampilkan data contoh.');
    //   }
  }, []);

  const loadInitialData = async () => {
    try {
        const [top, trend] = await Promise.all([
          axios.get('http://localhost:8083/api/dashboard/top-umkm'),
          axios.get("http://localhost:8082/api/analytics/trend-24h/all") 
        ]);

        // const [top, trend] = await Promise.all([
        //   axios.get('/api/dashboard/top-umkm'),
        //   axios.get('/api/analytics/trend-24h/all')
        // ]);
        if (top.data?.length) {
          setTopUmkm(top.data);
          setTrend24h(trend.data);
        }
     } catch (err) {
        setError('Backend belum siap, menampilkan data contoh.');
        
      }
  }

  const handleSearch = async (e) => {
    e.preventDefault();
    const url = selectedKecamatan  ? `http://localhost:8084/api/search/kecamatan/${selectedKecamatan}`: `http://localhost:8084/api/search?q=${query}`;
    // const url = selectedKecamatan  ? `/api/search/kecamatan/${selectedKecamatan}`: `/api/search?q=${query}`;
    const res = await axios.get(url);
    setSearchResults(res.data);
  }

  return (
    <div className="p-6 bg-gray-100 min-h-screen space-y-6">
      {error && (
        <div className="md:col-span-3 rounded border border-yellow-300 bg-yellow-50 p-3 text-sm text-yellow-700">
          <center>{error}</center>
        </div>
      )}
      {/* 1. HEADER + SEARCH + FILTER */}
      <div className="bg-white shadow rounded-xl p-4">
        <h1 className="text-2xl font-bold mb-3">JakUMKM Real-time Dashboard</h1>
        <form onSubmit={handleSearch} className="flex flex-col md:flex-row gap-2">
          <input
            value={query} onChange={(e) => setQuery(e.target.value)}
            placeholder="Cari UMKM atau Produk..."
            className="flex-1 border rounded-lg px-4 py-2"
          />
          <select value={selectedKecamatan} onChange={(e) => setSelectedKecamatan(e.target.value)}
            className="border rounded-lg px-4 py-2">
            <option value="">Semua Kecamatan</option>
            <option value="Jakarta Selatan">Jakarta Selatan</option>
            <option value="Jakarta Timur">Jakarta Timur</option>
            <option value="Jakarta Barat">Jakarta Barat</option>
            <option value="Jakarta Utara">Jakarta Utara</option>
          </select>
          <button className="bg-indigo-600 text-white px-4 py-2 rounded-lg flex items-center gap-2">
            <Search size={18}/> Cari
          </button>
        </form>
      </div>

       {/* 4. HASIL SEARCH */}
      {searchResults.length > 0 && (
        <div className="bg-white shadow rounded-xl p-4">
          <h2 className="font-bold mb-3">Hasil Pencarian</h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
            {searchResults.map((umkm) => (
              <div key={umkm.umkmId} className="border rounded-lg p-3 hover:shadow-md">
                <p className="font-semibold">{umkm.umkmName}</p>
                <p className="text-sm text-gray-600">{umkm.productName}</p>
                <p className="text-sm text-gray-500 flex items-center gap-1"><MapPin size={14}/> {umkm.kecamatan}</p>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* 2. KPI CARDS */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="bg-white shadow rounded-xl p-4 flex items-center gap-3">
          <DollarSign className="text-green-500" size={32}/>
          <div>
            <p className="text-gray-500">Total Penjualan Hari Ini</p>
            <p className="text-2xl font-bold">Rp {summary.totalSalesToday?.toLocaleString('id-ID')}</p>
          </div>
        </div>
        <div className="bg-white shadow rounded-xl p-4 flex items-center gap-3">
          <ShoppingCart className="text-blue-500" size={32}/>
          <div>
            <p className="text-gray-500">Total Order Hari Ini</p>
            <p className="text-2xl font-bold">{summary.totalOrdersToday}</p>
          </div>
        </div>
        <div className="bg-white shadow rounded-xl p-4 flex items-center gap-3">
          <TrendingUp className="text-orange-500" size={32}/>
          <div>
            <p className="text-gray-500">Rata-rata / Order</p>
            <p className="text-2xl font-bold">Rp {summary.avgOrderValue?.toLocaleString('id-ID')}</p>
          </div>
        </div>
      </div>

      {/* 3. GRAFIK REALTIME + TOP 5 */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
        <div className="lg:col-span-2 bg-white shadow rounded-xl p-4">
          <h2 className="font-bold mb-2">Penjualan 1 Menit Terakhir / Kecamatan</h2>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={mapData}>
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Bar dataKey="sales" fill="#4f46e5" radius={[8,8,0,0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
        <div className="bg-white shadow rounded-xl p-4">
          <h2 className="font-bold mb-2">Top 5 UMKM Hari Ini</h2>
          <ul className="space-y-2">
             
            {topUmkm.map((u, i) => (
              <li key={i} className="flex justify-between items-center">
                <span>{i+1}. {u.umkmName}</span>
                <span className="font-semibold text-indigo-600">Rp {u.totalSales?.toLocaleString('id-ID')}</span>
              </li>
            ))}
          </ul>
        </div>
      </div>

     

      {/* 5. TABEL TRANSAKSI REALTIME + GRAFIK 24J */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
        <div className="bg-white shadow rounded-xl p-4">
          <h2 className="font-bold mb-2">Transaksi Terbaru</h2>
          <table className="w-full text-sm">
            <thead><tr className="text-left text-gray-500"><th>UMKM</th><th>Rp</th></tr></thead>
            <tbody>
              {recentOrders.map((o,i) => (
                <tr key={i}><td>{o.umkmName}</td><td>Rp {o.amount?.toLocaleString('id-ID')}</td></tr>
              ))}
            </tbody>
          </table>
        </div>
        <div className="bg-white shadow rounded-xl p-4">
          <h2 className="font-bold mb-2">Trend Penjualan 24 Jam</h2>
          <ResponsiveContainer width="100%" height={200}>
            <LineChart data={trend24h}>
              <XAxis dataKey="hour" />
              <YAxis />
              <Tooltip />
              <Line type="monotone" dataKey="total" stroke="#4f46e5" strokeWidth={2} />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>

    </div>
  )
}