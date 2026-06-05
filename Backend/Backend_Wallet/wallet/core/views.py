from rest_framework.decorators import api_view
from rest_framework.response import Response
from django.contrib.auth.hashers import check_password

from .models import User, Wallet
from .serializers import RegisterSerializer
from django.contrib.auth import authenticate


@api_view(['POST'])
def register(request):

    serializer = RegisterSerializer(data=request.data)

    if not serializer.is_valid():
        return Response(
            serializer.errors,
            status=400)

    full_name = serializer.validated_data['full_name']
    mobile_number = serializer.validated_data['mobile_number']
    password = serializer.validated_data['password']

    if User.objects.filter(mobile_number=mobile_number).exists():

        return Response({
            "error": "Mobile number already exists"
        }, status=400)

    user = User.objects.create_user(
        mobile_number=mobile_number,
        password=password
    )

    user.full_name = full_name
    user.save()

    Wallet.objects.create(
        user=user
    )

    return Response({
        "message": "User registered successfully",
        "user_id": user.id
    })


@api_view(['POST'])
def login(request):

    mobile_number = request.data.get('mobile_number')
    password = request.data.get('password')

    if not mobile_number or not password:
        return Response(
            {"error": "Mobile number and password required"},
            status=400
        )

    user = authenticate(
        request,
        mobile_number=mobile_number,
        password=password
    )

    if user is None:
        return Response(
            {"error": "Invalid credentials"},
            status=401
        )

    wallet = Wallet.objects.get(user=user)

    return Response({
        "message": "Login successful",
        "user_id": user.id,
        "full_name": user.full_name,
        "mobile_number": user.mobile_number,
        "role": user.role,
        "wallet_balance": wallet.balance
    })