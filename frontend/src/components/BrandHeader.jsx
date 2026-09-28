export default function BrandHeader() {
  return (
    <header className="brand-header">
      <div className="brand-lockup">
        <div className="brand-mark" aria-hidden="true">
          <span className="brand-mark__orbit" />
          <span className="brand-mark__core">O</span>
        </div>
        <div>
          <p className="eyebrow">OMNITRIXS INTELLIGENCE</p>
          <h1>Orin</h1>
        </div>
      </div>
      <p className="brand-copy">Your focused desktop companion.</p>
    </header>
  );
}
