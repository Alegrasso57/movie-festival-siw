const root = document.getElementById('recensioni-root');
const filmId = root.getAttribute('data-film-id');
const username = root.getAttribute('data-username') || null;

function ListaRecensioni() {
    const [recensioni, setRecensioni] = React.useState([]);
    const [testo, setTesto] = React.useState('');
    const [voto, setVoto] = React.useState(5);
    const [errore, setErrore] = React.useState(null);
    const [modificaId, setModificaId] = React.useState(null);

    function caricaRecensioni() {
        fetch('/api/movies/' + filmId + '/reviews')
            .then(res => res.json())
            .then(data => setRecensioni(data))
            .catch(err => console.error(err));
    }

    React.useEffect(() => {
        caricaRecensioni();
    }, []);

    function inviaRecensione(e) {
        e.preventDefault();
        setErrore(null);

        const url = modificaId
            ? '/api/reviews/' + modificaId
            : '/api/movies/' + filmId + '/reviews';
        const method = modificaId ? 'PUT' : 'POST';

        fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ testo: testo, voto: parseInt(voto) })
        })
            .then(res => {
                if (!res.ok) {
                    return res.json().then(data => { throw new Error(data.errore || 'Errore'); });
                }
                return res.json();
            })
            .then(() => {
                setTesto('');
                setVoto(5);
                setModificaId(null);
                caricaRecensioni();
            })
            .catch(err => setErrore(err.message));
    }

    function elimina(id) {
        fetch('/api/reviews/' + id, { method: 'DELETE' })
            .then(res => res.json())
            .then(() => caricaRecensioni())
            .catch(err => console.error(err));
    }

    function iniziaModifica(recensione) {
        setModificaId(recensione.id);
        setTesto(recensione.testo);
        setVoto(recensione.voto);
    }

    return React.createElement('div', null,
        recensioni.map(r => React.createElement('div', { key: r.id, style: { borderBottom: '1px solid #ccc', marginBottom: '10px' } },
            React.createElement('p', null, 'Voto: ' + r.voto + '/10'),
            React.createElement('p', null, r.testo),
            React.createElement('p', null, 'di ' + r.autoreUsername + ' il ' + r.data),
            username === r.autoreUsername && React.createElement('span', null,
                React.createElement('button', { onClick: () => iniziaModifica(r) }, 'Modifica'),
                React.createElement('button', { onClick: () => elimina(r.id) }, 'Elimina')
            )
        )),
        username
            ? React.createElement('form', { onSubmit: inviaRecensione },
                React.createElement('h3', null, modificaId ? 'Modifica la tua recensione' : 'Scrivi una recensione'),
                errore && React.createElement('p', { style: { color: 'red' } }, errore),
                React.createElement('label', null, 'Voto (1-10): ',
                    React.createElement('input', {
                        type: 'number', min: 1, max: 10, value: voto,
                        onChange: e => setVoto(e.target.value)
                    })
                ),
                React.createElement('br'),
                React.createElement('label', null, 'Testo: ',
                    React.createElement('textarea', {
                        value: testo,
                        onChange: e => setTesto(e.target.value)
                    })
                ),
                React.createElement('br'),
                React.createElement('button', { type: 'submit' }, modificaId ? 'Salva modifiche' : 'Invia recensione')
            )
            : React.createElement('p', null,
                React.createElement('a', { href: '/login' }, 'Accedi'),
                ' per lasciare una recensione.'
            )
    );
}

ReactDOM.createRoot(root).render(React.createElement(ListaRecensioni));