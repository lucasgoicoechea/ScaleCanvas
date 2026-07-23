export function formatNumber(value: number, maximumFractionDigits = 2): string {
  return new Intl.NumberFormat('en-US', { maximumFractionDigits }).format(value)
}

export function titleize(value: string): string {
  return value
    .toLowerCase()
    .split('_')
    .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
    .join(' ')
}
