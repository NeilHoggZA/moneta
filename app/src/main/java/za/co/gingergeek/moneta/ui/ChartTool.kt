package za.co.gingergeek.moneta.ui

import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import za.co.gingergeek.moneta.models.SavedExchangeRate

/**
 * A simple extendable tool for charting a bar graph
 */
class ChartTool(
    private val barChart: BarChart
) {

    private lateinit var savedExchangeRates: List<SavedExchangeRate>

    /**
     * Called from the HomeFragment
     * This handles data transformation, display settings and
     * chart preparation before drawing the graph on screen
     */
    fun drawBarChart(savedExchangeRates: List<SavedExchangeRate>) {
        this.savedExchangeRates = savedExchangeRates
        if (savedExchangeRates.isNotEmpty()) {
            val barChartData = createChartData()
            configureChartAppearance()
            prepareChartData(barChartData)
        }
    }

    /**
     * Transforms savedExchangeRates data into data that
     * can be plotted on a bar graph
     */
    private fun createChartData(): BarData {
        val barData = arrayListOf<BarEntry>()
        // We only need the exchange rate
        barData.addAll(savedExchangeRates.mapIndexed { i, value ->
            BarEntry(i.toFloat(), value.exchangeRate)
        })
        val set = BarDataSet(barData, "Currency")
        val dataSets = arrayListOf<IBarDataSet>()
        dataSets.add(set)
        return BarData(dataSets)
    }

    /**
     * Determines how the bar graph looks
     */
    private fun configureChartAppearance() {
        barChart.description.isEnabled = false
        barChart.setDrawValueAboveBar(false)

        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)

        /* This is responsible for setting up the x-axis labels for each
        * bar to the iso-code representing the currency for that bar */
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                if (value.toInt() < savedExchangeRates.size) {
                    return savedExchangeRates[value.toInt()].isoCode
                }
                /* when a saved rate is deleted from the main screen, the last value index
                * is n+1, which causes an index out of bounds exception, to prevent this,
                * the only solution was to return an empty string when the current index == n+1 */
                return ""
            }
        }

        val axisLeft = barChart.axisLeft
        axisLeft.setDrawGridLines(false)
        axisLeft.granularity = 10f
        axisLeft.axisMinimum = 0f

        val axisRight = barChart.axisRight
        axisRight.setDrawGridLines(false)
        axisRight.granularity = 10f
        axisRight.axisMinimum = 0f
    }

    /**
     * Initializes bar chart data and prepares for display
     */
    private fun prepareChartData(data: BarData) {
        data.setValueTextSize(12f)
        barChart.data = data
        barChart.invalidate()
    }

}