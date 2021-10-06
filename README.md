# Highly Available & Reliable Messaging with RabbitMQ

Halo, teman - teman sekalian. pada kesempatan kali ini kami dari kelompok 19 Java EE akan memberikan tutorial beserta gambaran mengenai bagaimana cara untuk membangun sebuah aplikasi
yang berbasis MoM ( Message Oriented Middleware ) yang dapat bertahan dalam kondisi Ekstrim seperti Message Broker yang mati / down, Service yang mati / down, dan juga kami akan memberitahu
langkah - langkah atau algoritma apa yang dapat kita gunakan untuk mengatasi hal - hal ekstrim tersebut.

## HARDWARE SPECIFICATION
### :computer: PROCESSOR  > Intel Core I7 8550U CPU @ 1.80GHz (8 CPUs), ~ 2.0GHz
### :computer: RAM  > 8GB DDR4
### :computer: SSD/HDD  > 128GB / 1 TB
### :computer: VGA  > NVidia MX 150
### :computer: Operating System  > Windows 10 Pro 64-bit (10.0, Build 19043)

### :exclamation::exclamation: DISCLAIMER :
1. Spesifikasi diatas adalah spesifikasi hardware yang kami gunakan ketika menjalankan project kami.
2. Spesifikasi tersebut dapat berubah sesuai dengan device / hardware yang anda gunakan.
3. Spesifikasi diatas hanyalah sebuah referensi tambahan.



## SOFTWARE REQUIREMENT
### 1. :wrench: RabbitMQ Docker Image
### 2. :wrench: Visual Studio Code / Other IDE
### 3. :wrench: Java dan Python Environment
### 4. :wrench: Web Browser (Ex : OperaGx, Chrome, Mozilla,etc)
### 5. :wrench: POSTMAN
### 6. :wrench: Docker (Windows based/Linux based) depends on your Machine and Convenience.

# INSTALATION GUIDE
#### 1. Download terlebih dahulu file - file project yang ada di repositori ini.
#### 2. Setelah itu buka IDE kesayangan anda untuk membuka project Consumer dan Publisher
#### 3. Buka folder Consumer pada IDE kesayangan anda dan jalankan perintah berikut. Tunggu hingga proses selesai!
```docker
# Pastikan sebelum menjalankan perintah berikut ini. docker sudah berjalan pada device yang anda gunakan.
# Pastikan Koneksi internet anda tersedia pada saat melakukan eksekusi terhadap perintah berikut ini.
# Pastikan sistem operasi yang anda gunakan sudah terinstall docker. jika belum maka proses instalasi tidak akan bisa dilanjutkan.
docker build -t worker-script .
```
#### 4. Perintah diatas berfungsi untuk membentuk sebuah docker image dari consumer yang akan kita gunakan nantinya.

## CLUSTERING RABBITMQ
#### 1. Setelah anda berhasil mengeksekusi perintah sebelumnya. maka langkah berikutnya adalah melakukan konfigurasi terhadap cluster dari RabbitMQ
#### 2. Untuk melakukan proses clustering. Bukalah aplikasi Publisher pada IDE kesayangan anda dan jalankan perintah berikut ini :
```docker
docker run -d --rm --net rabbit `
-v ${PWD}/config/rabbit-1/:/config/ `
-e RABBITMQ_CONFIG_FILE=/config/rabbitmq `
-e RABBITMQ_ERLANG_COOKIE=WIWVHCDTCIUAWANLMQAW `
--hostname rabbit-1 `
--name rabbit-1 `
-p 15672:15672 `
-p 5672:5672 `
rabbitmq:3-management

docker run -d --rm --net rabbit `
-v ${PWD}/config/rabbit-2/:/config/ `
-e RABBITMQ_CONFIG_FILE=/config/rabbitmq `
-e RABBITMQ_ERLANG_COOKIE=WIWVHCDTCIUAWANLMQAW `
--hostname rabbit-2 `
--name rabbit-2 `
-p 15673:15672 `
-p 5673:5672 `
rabbitmq:3-management


docker exec -it rabbit-1 rabbitmq-plugins enable rabbitmq_federation 
docker exec -it rabbit-2 rabbitmq-plugins enable rabbitmq_federation
```
#### 3. Tunggu hingga proses selesai. potongan perintah diatas digunakan untuk membuat sebuah cluster dengan dua buah instance RabbitMQ ( rabbit-1 & rabbit-2 ).

## CLUSTER MESSAGE AND QUEUE MIRRORING
#### 1. Selanjutnya, Kita akan menerapkan beberapa policy untuk dapat membuat sebuah proses Mirroring Queue.
#### 2. Jalankan perintah berikut ini untuk menerapkan Classic Mirroring pada Cluster.
```docker
# Mirroring
docker exec -it rabbit-1 bash
rabbitmqctl set_policy ha-fed \
    ".*" '{"federation-upstream-set":"all", "ha-mode":"nodes", "ha-params":["rabbit@rabbit-1","rabbit@rabbit-2"]}' \
    --priority 1 \
    --apply-to queues

rabbitmqctl set_policy ha-fed \
    ".*" '{"federation-upstream-set":"all", "ha-sync-mode":"automatic", "ha-mode":"nodes", "ha-params":["rabbit@rabbit-1","rabbit@rabbit-2"]}' \
    --priority 1 \
    --apply-to queues
```
#### 3. Potongan perintah docker diatas digunakan untuk menerapkan Ha-Policy Federation kepada setiap Queues yang ada di setiap Nodes Cluster RabbitMQ dan juga menerapkan auto-syncronize untuk setiap Nodes.
#### :exclamation::exclamation: DEPRECATED :exclamation::exclamation:
#### Metode yang kami gunakan pada praktikum kali ini mungkin sudah memiliki solusi atau cara yang lebih baru. Berikut referensi yang dapat anda gunakan https://www.rabbitmq.com/cluster-formation.html

## Last Step 😃
Jika semua proses sudah dilakukan tanpa ada kendala. maka, project sudah bisa digunakan !!! 
Untuk menggunakan project terlebih dahulu anda menjalankan aplikasi publisher dengan IDE Kesayangan anda. Kemudian, setelah Spring MVC nya sudah berjalan pada port 8080 maka bukalah Command Prompt anda dan jalankan perintah docker berikut ini untuk menjalankan Worker / Consumer yang akan mengconsume message yang dihasilkan publisher nantinya :
```docker
# Run Worker
docker run --it --name worker1 -e RABBIT_HOST=rabbit-1 --net rabbit worker-script
docker run --it --name worker2 -e RABBIT_HOST=rabbit-2 --net rabbit worker-script
```

# Additional Information & Useful Info
### CLEAN UP DOCKER
```docker
docker rm -f rabbit-1
docker rm -f rabbit-2
docker rm -f worker1
docker rm -f worker2
```

### OUR REFERENCE
#### Automatic Mirror Synchronizing : https://www.rabbitmq.com/ha.html#unsynchronised-mirrors
#### How to Make Cluster !! : https://www.rabbitmq.com/cluster-formation.html
#### RabbitMQ Documentation : https://www.rabbitmq.com/documentation.html
#### Retry Mechanism : https://howtodoinjava.com/spring-boot2/spring-retry-module/

### :heart::heart:DEMO APPLICATION : https://www.youtube.com/watch?v=hWz8KvJRIy8

&nbsp;
&nbsp;
&nbsp;
# :heart::heart: THANK YOU :heart::heart:
