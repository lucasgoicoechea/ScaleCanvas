interface NumberFieldProps {
  label: string
  value: number
  min?: number
  max?: number
  step?: number
  suffix?: string
  onChange: (value: number) => void
}

export function NumberField({ label, value, min, max, step = 1, suffix, onChange }: NumberFieldProps) {
  return (
    <label className="field">
      <span>{label}</span>
      <div className="input-with-suffix">
        <input
          type="number"
          value={value}
          min={min}
          max={max}
          step={step}
          onChange={(event) => onChange(Number(event.target.value))}
        />
        {suffix && <small>{suffix}</small>}
      </div>
    </label>
  )
}
