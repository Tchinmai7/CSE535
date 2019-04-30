from django.http import HttpResponse
from rest_framework.parsers import FileUploadParser
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework import status

from .serializers import LoginSerializer, RegisterSerializer
from .driver import authUser
import random

class LoginView(APIView):
    parser_class = (FileUploadParser,)

    def post(self, request, *args, **kwargs):

        file_serializer = LoginSerializer(data=request.data)

        if file_serializer.is_valid():
            file_serializer.save()
            print(file_serializer.data)
            return Response(authUser(file_serializer.data), status=status.HTTP_200_OK)
        else:
            return Response(file_serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def get(self, request):
        return HttpResponse(random.uniform(65.5, 70.2))

class RegisterView(APIView):
    parser_class = (FileUploadParser,)

    def post(self, request, *args, **kwargs):

        file_serializer = RegisterSerializer(data=request.data)

        if file_serializer.is_valid():
            file_serializer.save()
            print(file_serializer.data)
            return Response(authUser(file_serializer.data), status=status.HTTP_200_OK)
        else:
            return Response(file_serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def get(self, request):
        return HttpResponse(random.uniform(65.5, 70.2))
