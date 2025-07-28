import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { BookOpenIcon, UserIcon, LogOutIcon, SearchIcon, MenuIcon, XIcon } from 'lucide-react';

const Navbar: React.FC = () => {
  const location = useLocation();
  const { usuarioAtual, logout, ehAdmin } = useAuth();
  const navigate = useNavigate();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');

  const handleLogout = () => {
    const isAdmin = ehAdmin;
    
    logout();
    setSearchQuery('');
    
    if (isAdmin) {
      navigate('/admin-login');
    } else {
      navigate('/login');
    }
  };

  const shouldShowNavContent = location.pathname !== '/login' && location.pathname !== '/register' && location.pathname !== '/admin-login';

  return (
    <nav className="bg-blue-600 text-white shadow-md">
      <div className="container mx-auto px-4">
        <div className="flex justify-between items-center py-3">
          <Link to={"/home"} className="flex items-center space-x-2">
            <BookOpenIcon className="h-6 w-6" />
            <span className="text-xl font-bold">Bookworms</span>
          </Link>

          {shouldShowNavContent && (
            <div className="hidden md:flex items-center space-x-4">
              {usuarioAtual ? (
                <>
                  <Link to="/profile" className="hover:text-blue-200 flex items-center space-x-1">
                    <UserIcon className="h-5 w-5" />
                    <span>Perfil</span>
                  </Link>
                  <button onClick={handleLogout} className="hover:text-blue-200 flex items-center space-x-1">
                    <LogOutIcon className="h-5 w-5" />
                    <span>Sair</span>
                  </button>
                </>
              ) : null}
            </div>
          )}
          {shouldShowNavContent && (
            <button className="md:hidden" onClick={() => setIsMenuOpen(!isMenuOpen)}>
              {isMenuOpen ? <XIcon className="h-6 w-6" /> : <MenuIcon className="h-6 w-6" />}
            </button>
          )}
        </div>

        {isMenuOpen && shouldShowNavContent && (
          <div className="md:hidden pb-4 space-y-3">
            {usuarioAtual ? (
              <>
                <Link to="/profile" className="block py-2 hover:bg-blue-700 rounded px-3" onClick={() => setIsMenuOpen(false)}>
                  Perfil
                </Link>
                <button onClick={() => { handleLogout(); setIsMenuOpen(false); }} className="block w-full text-left py-2 hover:bg-blue-700 rounded px-3">
                  Sair
                </button>
              </>
            ) : null}
          </div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;