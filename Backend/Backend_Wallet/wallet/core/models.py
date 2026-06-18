from django.db import models
from django.contrib.auth.models import (
    AbstractBaseUser,
    PermissionsMixin,
    BaseUserManager
)


class UserManager(BaseUserManager):

    def create_user(self, mobile_number, password=None, role='MEMBER'):
        if not mobile_number:
            raise ValueError("Mobile number is required")

        user = self.model(mobile_number=mobile_number, role=role)

        user.set_password(password)
        user.save(using=self._db)

        return user

    def create_superuser(self, mobile_number, password):

        user = self.create_user(
            mobile_number=mobile_number,
            password=password,
            role='ADMIN'
        )

        user.is_staff = True
        user.is_superuser = True

        user.save(using=self._db)

        return user


class User(AbstractBaseUser, PermissionsMixin):

    ROLE_CHOICES = (
        ('ADMIN', 'Admin'),
        ('MEMBER', 'Member'),
    )

    mobile_number = models.CharField(max_length=15,unique=True)

    full_name = models.CharField(max_length=100)

    role = models.CharField(max_length=10,choices=ROLE_CHOICES,default='MEMBER')

    is_active = models.BooleanField(default=True)
    is_staff = models.BooleanField(default=False)

    created_at = models.DateTimeField(auto_now_add=True)

    fcm_token = models.TextField(
        null=True,
        blank=True
    )

    objects = UserManager()

    USERNAME_FIELD = 'mobile_number'
    REQUIRED_FIELDS = []

    def __str__(self):
        return self.mobile_number


class Wallet(models.Model):

    user = models.OneToOneField(User,on_delete=models.CASCADE,related_name='wallet')

    balance = models.DecimalField(
        max_digits=12,
        decimal_places=2,
        default=0
    )

    updated_at = models.DateTimeField(
        auto_now=True
    )

    def __str__(self):
        return f"{self.user.mobile_number} - {self.balance}"



class FundRequest(models.Model):

    REQUEST_TYPES = (
        ('ADD', 'Add Fund'),
        ('WITHDRAW', 'Withdraw Fund'),
        ('BONUS', 'Bonus'),
    )

    STATUS_TYPES = (
        ('PENDING', 'Pending'),
        ('APPROVED', 'Approved'),
        ('REJECTED', 'Rejected'),
        
    )

    user = models.ForeignKey(User,on_delete=models.CASCADE,related_name='fund_requests')

    amount = models.DecimalField(max_digits=12,decimal_places=2)

    request_type = models.CharField(max_length=20,choices=REQUEST_TYPES)

    status = models.CharField(max_length=20,choices=STATUS_TYPES, default='PENDING')
    utr_number = models.CharField(max_length=100,blank=True,null=True)
    upi_id = models.CharField(max_length=100,blank=True,null=True)

    qr_code = models.ImageField(upload_to="withdraw_qr/",blank=True,null=True)

    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.user.mobile_number} - {self.request_type} - {self.amount}"
    


class QRCode(models.Model):

    image = models.ImageField(
        upload_to='qr_codes/'
    )

    uploaded_at = models.DateTimeField(
        auto_now_add=True
    )

    def __str__(self):
        return "Wallet QR Code"
    

class ChatRoom(models.Model):

    user = models.ForeignKey(
        User,
        on_delete=models.CASCADE,
        related_name="chat_rooms"
    )

    created_at = models.DateTimeField(
        auto_now_add=True
    )

    updated_at = models.DateTimeField(
        auto_now=True
    )

    def __str__(self):

        return self.user.full_name
    
class Message(models.Model):

    room = models.ForeignKey( ChatRoom,on_delete=models.CASCADE,related_name="messages")

    sender = models.ForeignKey(User,on_delete=models.CASCADE)

    message = models.TextField()

    is_read = models.BooleanField(
        default=False
    )

    created_at = models.DateTimeField(
        auto_now_add=True
    )

    def __str__(self):

        return f"{self.sender.full_name}: {self.message}"