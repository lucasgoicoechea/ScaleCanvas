import { Background, Controls, MarkerType, ReactFlow, type Edge, type Node } from '@xyflow/react'
import '@xyflow/react/dist/style.css'
import type { RuleOutcome } from '../types'
import { titleize } from '../formatters'

const positions = [
  { x: 30, y: 80 }, { x: 280, y: 20 }, { x: 280, y: 140 }, { x: 530, y: 20 },
  { x: 530, y: 140 }, { x: 780, y: 20 }, { x: 780, y: 140 }, { x: 1030, y: 80 },
]

export function ArchitectureCanvas({ recommendations }: { recommendations: RuleOutcome[] }) {
  const active = recommendations.filter((item) => item.urgency !== 'NOT_YET').slice(0, 8)
  const nodes: Node[] = [
    { id: 'user', position: { x: -190, y: 80 }, data: { label: 'Users / Clients' }, type: 'input', className: 'flow-node user-node' },
    ...active.map((item, index) => ({
      id: item.ruleId,
      position: positions[index] ?? { x: 280 + index * 180, y: 80 },
      data: { label: titleize(item.component) },
      className: `flow-node ${item.urgency.toLowerCase()}`,
    })),
  ]
  const sequence = nodes.map((node) => node.id)
  const edges: Edge[] = sequence.slice(1).map((target, index) => ({
    id: `edge-${sequence[index]}-${target}`,
    source: sequence[index],
    target,
    markerEnd: { type: MarkerType.ArrowClosed },
    animated: index < 2,
  }))

  function exportSvg() {
    const svg = document.querySelector('.react-flow__viewport svg') as SVGSVGElement | null
    if (!svg) return
    const clone = svg.cloneNode(true) as SVGSVGElement
    clone.setAttribute('xmlns', 'http://www.w3.org/2000/svg')
    clone.setAttribute('xmlns:xlink', 'http://www.w3.org/1999/xlink')
    const serializer = new XMLSerializer()
    const source = serializer.serializeToString(clone)
    const blob = new Blob([source], { type: 'image/svg+xml;charset=utf-8' })
    const url = URL.createObjectURL(blob)
    const anchor = document.createElement('a')
    anchor.href = url
    anchor.download = 'architecture-canvas.svg'
    anchor.click()
    URL.revokeObjectURL(url)
  }

  async function exportPng() {
    const svg = document.querySelector('.react-flow__viewport svg') as SVGSVGElement | null
    if (!svg) return
    const clone = svg.cloneNode(true) as SVGSVGElement
    clone.setAttribute('xmlns', 'http://www.w3.org/2000/svg')
    clone.setAttribute('xmlns:xlink', 'http://www.w3.org/1999/xlink')
    const serializer = new XMLSerializer()
    const source = serializer.serializeToString(clone)
    const svgBlob = new Blob([source], { type: 'image/svg+xml;charset=utf-8' })
    const url = URL.createObjectURL(svgBlob)
    await new Promise<void>((resolve, reject) => {
      const image = new Image()
      image.onload = () => {
        const canvas = document.createElement('canvas')
        canvas.width = image.width || 1200
        canvas.height = image.height || 800
        const ctx = canvas.getContext('2d')
        if (!ctx) return reject(new Error('Canvas context unavailable'))
        ctx.fillStyle = '#0a0103'
        ctx.fillRect(0, 0, canvas.width, canvas.height)
        ctx.drawImage(image, 0, 0)
        const pngUrl = canvas.toDataURL('image/png')
        const anchor = document.createElement('a')
        anchor.href = pngUrl
        anchor.download = 'architecture-canvas.png'
        anchor.click()
        URL.revokeObjectURL(url)
        resolve()
      }
      image.onerror = () => {
        URL.revokeObjectURL(url)
        reject(new Error('SVG load failed'))
      }
      image.src = url
    })
  }

  return (
    <section className="visual-card architecture-card">
      <div className="section-title">
        <div><div><p className="eyebrow">Suggested shape</p><h3>Architecture canvas</h3></div></div>
        <div style={{ display: 'flex', gap: 6 }}>
          <button className="secondary-button compact" onClick={exportSvg} type="button">Export SVG</button>
          <button className="secondary-button compact" onClick={exportPng} type="button">Export PNG</button>
        </div>
      </div>
      <div className="flow-wrapper">
        <ReactFlow nodes={nodes} edges={edges} fitView minZoom={0.35} maxZoom={1.5}>
          <Background gap={18} size={1} />
          <Controls showInteractive={false} />
        </ReactFlow>
      </div>
    </section>
  )
}
