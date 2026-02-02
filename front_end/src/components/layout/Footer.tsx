const Footer = () => {
    const currentYear = new Date().getFullYear();
    return (
        <footer style={{ textAlign: 'center', padding: '1rem', backgroundColor: 'darkseagreen' }}>
            <p>&copy; {currentYear} Second assignment.</p>
        </footer>
    );
}

export default Footer;