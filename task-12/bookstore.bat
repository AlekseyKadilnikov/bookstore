FOR %%X IN (*.sql) DO mysql -h localhost -u root -p < %%X