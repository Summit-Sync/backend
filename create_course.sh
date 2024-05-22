xh -a $(./get_token.sh) -A bearer 'http://localhost:8080/api/v1/price' name=asd price:=123
xh -a $(./get_token.sh) -A bearer 'http://localhost:8080/api/v1/qualification' name='Erste Hilfe'
xh -a $(./get_token.sh) -A bearer 'http://localhost:8080/api/v1/qualification' name='Erste Hilfe'
xh -a $(./get_token.sh) -A bearer 'http://localhost:8080/api/v1/qualification' name='Erste Hilfe'
xh -a $(./get_token.sh) -A bearer 'http://localhost:8080/api/v1/location' street="Gastfeldstr. 12" postcode="49312" title="Main address" city="My City"
xh -a $(./get_token.sh) -A bearer 'http://localhost:8080/api/v1/trainer' username=Test-Asd-asdsafwqasfd132r12312asdsa3 password=1234 firstName=Lukas lastName=Müller email=test@mail.sadwqfw phone=t
xh -a $(./get_token.sh) -A bearer 'http://localhost:8080/api/v1/course' acronym=kv 'dates:=["2024-05-13T10:00:25.739Z", "2024-05-13T10:00:40.456Z", "2024-08-01T09:00:00.000Z"]'  description=kvBeschreibrung duration:=40 location:=1 meetingPoint=kvTreffpunkt notes=test numberParticipants:=2 numberTrainers:=2 numberWaitlist:=2 'prices:=[1]' 'requiredQualifications:=[1]' title=kvTitle visible:=false

