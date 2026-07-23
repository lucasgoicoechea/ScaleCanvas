import ReactECharts from 'echarts-for-react'
import { useMemo } from 'react'

interface CloudCostItem {
  serviceName: string
  cloudProvider: string
  serviceType: string
  region: string
  unitMonthlyCost: number
  quantity: number
  monthlySubtotal: number
  yearlySubtotal: number
  driver: string
}

interface CloudCostBreakdownProps {
  items: CloudCostItem[]
}

export function CloudCostBreakdown({ items }: CloudCostBreakdownProps) {
  const chartData = useMemo(() => {
    return items.map((item) => ({
      serviceName: item.serviceName,
      monthly: Number(item.monthlySubtotal.toFixed(2)),
      yearly: Number(item.yearlySubtotal.toFixed(2)),
    }))
  }, [items])

  const option = useMemo(() => {
    return {
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' },
      },
      legend: {
        data: ['Monthly', 'Yearly'],
        textStyle: { color: '#f87171' },
      },
      grid: { left: 40, right: 20, top: 60, bottom: 40 },
      xAxis: {
        type: 'category',
        data: chartData.map((item) => item.serviceName),
        axisLine: { lineStyle: { color: '#7f1d3d' } },
        axisLabel: { color: '#f87171', rotate: 30 },
      },
      yAxis: {
        type: 'value',
        name: 'Cost ($)',
        axisLine: { lineStyle: { color: '#7f1d3d' } },
        axisLabel: { color: '#f87171' },
        splitLine: { lineStyle: { color: '#3b1525' } },
      },
      series: [
        { name: 'Monthly', type: 'bar', data: chartData.map((item) => item.monthly), itemStyle: { color: '#dc2626' } },
        { name: 'Yearly', type: 'bar', data: chartData.map((item) => item.yearly), itemStyle: { color: '#991b1b' } },
      ],
    }
  }, [chartData])

  return (
    <div className="visual-card">
      <div className="section-title">
        <span>Cloud cost breakdown</span>
      </div>
      {items.length === 0 && <p className="column-note">Load or create a scenario to estimate cloud costs.</p>}
      {items.length > 0 && (
        <>
          <ReactECharts option={option} style={{ height: 320 }} />
          <div className="equalizer-insight">
            <strong>Interpretation:</strong> monthly and yearly subtotals are estimated from compute/storage unit prices by provider and region. Use them as starting points, not binding quotes.
          </div>
        </>
      )}
    </div>
  )
}
