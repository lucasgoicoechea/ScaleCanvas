import { describe, expect, it } from 'vitest'
import { titleize } from './formatters'

describe('titleize', () => {
  it('converts enum identifiers to readable labels', () => {
    expect(titleize('MODULAR_MONOLITH')).toBe('Modular Monolith')
  })
})
