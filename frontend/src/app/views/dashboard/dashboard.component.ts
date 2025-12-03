import { Component, OnInit, ViewChild } from '@angular/core';
import { InvoiceService } from '../../services/invoice.service';
import { ClientService } from '../../services/client.service';
import { ProductService } from '../../services/product.service';
import { BaseChartDirective } from 'ng2-charts';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  standalone: false
})
export class DashboardComponent implements OnInit {

  totalInvoiced: number = 0;
  invoicesThisMonth: number = 0;
  invoicesToday: number = 0;

  @ViewChild(BaseChartDirective) chart?: BaseChartDirective;
  chartType: 'bar' = 'bar';
  chartData: any = {
    labels: [],
    datasets: [
      { data: [], label: 'Invoices' }
    ]
  };
  chartOptions = {
    responsive: true,
    plugins: {
      legend: { display: true },
      tooltip: { enabled: true }
    }
  };

  latestInvoices: any[] = [];
  topClients: any[] = [];
  topProducts: any[] = [];

  constructor(
    private invoiceService: InvoiceService,
    private clientService: ClientService,
    private productService: ProductService
  ) { }

  ngOnInit(): void {
    this.loadKPIs();
    this.loadChart();
    this.loadRecentData();
  }

  private loadKPIs(): void {
    this.invoiceService.getTotalInvoiced().subscribe(v => this.totalInvoiced = v);
    this.invoiceService.getInvoicesThisMonth().subscribe(v => this.invoicesThisMonth = v);
    this.invoiceService.getInvoicesToday().subscribe(v => this.invoicesToday = v);
  }

  private loadChart(): void {
    this.invoiceService.getMonthlyInvoices().subscribe(data => {
      this.chartData.labels = data.map(d => d.monthName);
      this.chartData.datasets[0].data = data.map(d => d.total);

      setTimeout(() => this.chart?.update(), 0);
    });
  }

  private loadRecentData(): void {
    this.invoiceService.getLatestInvoices().subscribe(f => this.latestInvoices = f);
    this.clientService.getTopClients().subscribe(c => this.topClients = c);
    this.productService.getTopProducts().subscribe(p => this.topProducts = p);
  }

}
