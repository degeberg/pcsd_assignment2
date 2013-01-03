x = [1, 5, 15, 50, 100, 250, 500];
trans = 10.*x;

y_latency_log = [28050891, 50955158, 126745638, 261951906, 761000960, 1432871212, 3074847156];
total_log = [303455136, 474789276, 1172074346, 2917995417, 5970862315, 13645212505, 30303095298];

y_latency = [16206225, 52383767, 100099510, 372195364, 518105076, 844113734, 2279466792];
total = [286789371, 448524283, 1032779591, 3057385342, 5983129639, 13178675833, 26699406148];

y_throughput_log = trans ./ total_log;
y_throughput = trans ./ total;


figure(1);
hold on;
xlabel('Number of clients')
ylabel('Latency (ns)')
plot(x, y_latency_log, 'r');
plot(x, y_latency, 'g');
hold off;
print -depsc 'latency'

figure(2);
hold on;
xlabel('Number of clients')
ylabel('Throughput (values/ns)')
plot(x, y_throughput_log, 'r');
plot(x, y_throughput, 'g');
hold off;
print -depsc 'throughput'
